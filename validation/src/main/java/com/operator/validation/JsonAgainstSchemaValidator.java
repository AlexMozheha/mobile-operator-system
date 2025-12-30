package com.operator.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JsonAgainstSchemaValidator {

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonSchemaFactory schemaFactory =
            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

    /*Використовується map `schemaCache` щоб не завантажувати одну й ту ж схему двічі.
      Ключ: шлях до файлу схеми від resources
      Значення: об’єкт JsonSchema для валідації*/
    private final Map<String, JsonSchema> schemaCache = new HashMap<>();

    public void validate(String schemaFile, String... jsonFileNames) {
        try (InputStream schemaStream = getClass().getResourceAsStream("/" + schemaFile)) {

            if (schemaStream == null)
                throw new IllegalArgumentException("Missing file: " + schemaFile);

            JsonSchema schema = loadSchemaFromClasspath(schemaFile);

            //JsonSchema schema = schemaFactory.getSchema(mapper.readTree(schemaStream));

            for (String jsonFileName : jsonFileNames){
                validate(schema, jsonFileName);
            }

        } catch (Exception e) {
            System.err.println("Error validating " + schemaFile + ": " + e.getMessage());
        }
    }

    public void validateString(String jsonContent, String schemaFile) throws IOException {
        String cleanJson = jsonContent.trim();

        // 2. Якщо Swagger прислав JSON загорнутий у лапки як рядок (наприклад: "{\"id\":1}")
        if (cleanJson.startsWith("\"") && cleanJson.endsWith("\"")) {
            // Десеріалізуємо рядок у нормальний JSON-текст
            cleanJson = mapper.readValue(cleanJson, String.class);
        }

        JsonSchema schema = loadSchemaFromClasspath(schemaFile);
        JsonNode json = mapper.readTree(cleanJson); // Тепер тут буде чистий об'єкт {}

        Set<ValidationMessage> errors = schema.validate(json);
        if (!errors.isEmpty()) {
            String message = errors.stream()
                    .map(ValidationMessage::getMessage)
                    .collect(Collectors.joining("; "));
            // Викидаємо 400 помилку з переліком проблем
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "JSON Schema Error: " + message);
        }
    }

    private void validate(JsonSchema schema, String jsonFile) {
        System.out.printf("Validating JSON... '%s'%n", jsonFile);
        try (InputStream jsonStream = getClass().getResourceAsStream("/" + jsonFile)) {

            if (jsonStream == null)
                throw new IllegalArgumentException("Missing file: " + jsonFile);

            JsonNode json = mapper.readTree(jsonStream);

            Set<ValidationMessage> errors = schema.validate(json);
            if (errors.isEmpty())
                System.out.printf(" %s is valid.%n", jsonFile);
            else {
                System.out.printf(" %s is invalid:%n", jsonFile);
                errors.forEach(err -> System.out.println("  - " + err.getMessage()));
            }

        } catch (Exception e) {
            System.err.println("Error validating " + jsonFile + ": " + e.getMessage());
        }

    }


    private JsonSchema loadSchemaFromClasspath(String schemaPath) throws IOException {
        if (schemaCache.containsKey(schemaPath)) {
            return schemaCache.get(schemaPath);
        }

        try (InputStream schemaStream = getClass().getResourceAsStream("/" + schemaPath)) {
            if (schemaStream == null) {
                throw new IllegalArgumentException("Missing schema file: " + schemaPath);
            }

            JsonNode schemaNode = mapper.readTree(schemaStream);

            // Визначається папка, де знаходиться схема, для резолва відносних $ref. Береться все, що до першого '/'
            String parentFolder = "";
            int lastSlash = schemaPath.lastIndexOf('/');
            if (lastSlash != -1) {
                parentFolder = schemaPath.substring(0, lastSlash);
            }

            // Рекурсивно резолвиться(підставляється замість "$ref" те, на що вона посилається
            // (рекурсивно, щоб, якщо в середині файлу, на який посилаються також є "$ref",
            // то процедура "резолвингу" спочатку відпрацювала в внутрішньому файлі)) всі $ref щодо папки схеми
            resolveRefs(schemaNode, parentFolder);

            JsonSchema schema = schemaFactory.getSchema(schemaNode);
            schemaCache.put(schemaPath, schema);
            return schema;
        }
    }

    /*Якщо нода має посилання "$ref" розділяється на частини за знаком '#'(Те, що перед '#' посилання на файл, після на Json Pointer)
    * Якщо є папка, яка в refPath, то додається батьківська тека, та читається .getResourceAsStream*/
    private void resolveRefs(JsonNode node, String parentFolder) throws IOException {
        if (node.isObject()) {
            if (node.has("$ref")) {
                String ref = node.get("$ref").asText();
                if (!ref.startsWith("http")) {
                    String[] parts = ref.split("#");
                    String refFile = parts[0];
                    String pointer = parts.length > 1 ? parts[1] : "";

                    String refPath = parentFolder.isEmpty() ? refFile : parentFolder + "/" + refFile;
                    try (InputStream refStream = getClass().getResourceAsStream("/" + refPath)) {
                        if (refStream == null) {
                            throw new IllegalArgumentException("Missing referenced schema: " + refPath);
                        }
                        JsonNode refNode = mapper.readTree(refStream);

                        // Якщо є JSON Pointer, переходимо до потрібного вузла
                        if (!pointer.isEmpty()) {
                            pointer = pointer.startsWith("/") ? pointer : "/" + pointer;
                            refNode = refNode.at(pointer);
                            if (refNode.isMissingNode()) {
                                throw new IllegalArgumentException("Invalid JSON Pointer in $ref: " + ref);
                            }
                        }

                        ((com.fasterxml.jackson.databind.node.ObjectNode) node).remove("$ref");
                        ((com.fasterxml.jackson.databind.node.ObjectNode) node).setAll((com.fasterxml.jackson.databind.node.ObjectNode) refNode);

                        // рекурс
                        resolveRefs(refNode, parentFolder);
                    }
                }
            }
            /*Рекурсивно проходиться все дерево JsonNode - шукаючи $ref в кожному об'єкті і масиві.
            Якщо поточний вузол - об'єкт, перевіряємо: чи є $ref серед його полів. Якщо є - підставляємо вміст посилання.
            Якщо поточний вузол - масив, перевіряємо кожен елемент масиву (елемент теж може бути об'єктом з $ref).
            Для будь-якого вкладеного об'єкта або масиву викликаємо ту ж функцію рекурсивно.*/
            node.fields().forEachRemaining(entry -> {
                try {
                    resolveRefs(entry.getValue(), parentFolder);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (node.isArray()) {
            for (JsonNode item : node) {
                resolveRefs(item, parentFolder);
            }
        }
    }


/*    public <T> T validateAndMap(String schemaFileName, String jsonFileName, Class<T> entityClass) {
        try {
            JsonSchema schema = getJsonSchema("/" + schemaFileName);

            JsonNode jsonNode = readJson(jsonFileName);
            if (!isValid(schema, jsonNode, jsonFileName)) {
                return null;
            }

            return mapJsonToObject(jsonNode, entityClass);

        } catch (IOException | ProcessingException e) {
            log.error("Error processing file '{}': {}", jsonFileName, e.getMessage());
            return null;
        }
    }


    private JsonNode readJson(String jsonFileName) throws IOException {
        try (InputStream jsonStream = getClass().getResourceAsStream("/" + jsonFileName)) {
            if (jsonStream == null) {
                throw new IOException("File not found: " + jsonFileName);
            }
            return mapper.readTree(jsonStream);
        }
    }

    private boolean isValid(JsonSchema schema, JsonNode jsonNode, String jsonFileName)
            throws ProcessingException {
        ProcessingReport report = schema.validate(jsonNode);
        if (!report.isSuccess()) {
            log.warn("JSON '{}' failed validation:", jsonFileName);
            printReport(report);
            return false;
        }
        System.out.printf("JSON file '%s' is valid.%n", jsonFileName);
        return true;
    }

    private <T> T mapJsonToObject(JsonNode jsonNode, Class<T> entityClass) throws IOException {
        return mapper.treeToValue(jsonNode, entityClass);
    }*/

    /*private JsonSchema getJsonSchema(String schemaFileName) throws ProcessingException, IOException {
//        JsonOrderValidationExample.class.getResource(schemaFileName)
        try (InputStream schemaStream = JsonAgainstSchemaValidator.class
                .getResourceAsStream(schemaFileName)) {
            if (schemaStream == null) {
                System.err.printf("Fail to load schema: %s%n", schemaFileName);
                throw new IOException("Fail to load schema: " + schemaFileName);
            }
            JsonNode schemaNode = mapper.readTree(schemaStream);
            if (schemaNode == null) {
                throw new ProcessingException("SchemaNode is null");
            }

            // Get the schema factory
//            final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
                    .setValidationConfiguration(ValidationConfiguration.newBuilder()
                            .addLibrary("https://json-schema.org/draft/2020-12/schema#",
                                    Library.newBuilder().freeze()).freeze()).freeze();
            return factory.getJsonSchema(schemaNode);
        }
    }

    private void printReport(ProcessingReport report) {
        if (report.isSuccess()) {
            System.out.println("Validation successful.");
        } else {
            System.out.println("Validation failed.");
            report.forEach(message -> System.out.println("  - " + message));
        }
        System.out.println();
    }*/
}

