package com.operator.validation;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Component
@Slf4j
public class XmlAgainstSchemaValidator {

    @Value("${xml.schema.version:http://www.w3.org/2001/XMLSchema}")
    String xmlSchemaVersion;

    public void validate(String schemaFileName, String... xmlFileNames) {

        try {
            // obtain schema
            Schema schema = getSchema(schemaFileName);
            // creating validator
            Validator validator = schema.newValidator();
            // run validation for each file
            for (String xmlFileName : xmlFileNames) {
                validate(validator, xmlFileName);
            }

        } catch (Exception e) {
            log.error("Cannot create Schema for {} because {}\nAborted...", schemaFileName, e.getMessage());
//            throw new RuntimeException(e);
        }
    }

    public void validateString(String xmlContent, String schemaFileName) throws SAXException, IOException {
        try {
            Schema schema = getSchema(schemaFileName);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(new java.io.StringReader(xmlContent));
            validator.validate(source);
        } catch (org.xml.sax.SAXException ex) {
            // Ловимо помилку схеми (наприклад, від'ємне число) і перетворюємо на 400 статус
            log.warn("XML Validation failed: {}", ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "XML Schema Error: " + ex.getMessage());
        }

    }

    /*public <T> T validateAndMap(String schemaFileName, String xmlFileName, Class<T> entityClass) {

        try {
            Schema schema = getSchema(schemaFileName);
            Validator validator = schema.newValidator();
            if(validate(validator, xmlFileName)){
            return mapXmlToObject(xmlFileName, entityClass);}

        } catch (Exception e) {
            log.error("Cannot create Schema for {} because {}\nAborted...", schemaFileName, e.getMessage());
        }
        return null;
    }*/

    private Schema getSchema(String schemaFileName) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(xmlSchemaVersion);


        sf.setResourceResolver((type, namespaceURI, publicId, systemId, baseURI) -> {
            try {
                // Витягуємо лише назву файлу (без шляху)
                String simpleName = systemId.substring(systemId.lastIndexOf('/') + 1);

                // Пробуємо знайти файл у тій же папці, що і головна схема
                String basePath = "/" + schemaFileName.substring(0, schemaFileName.lastIndexOf('/') + 1);
                InputStream resourceAsStream = getClass().getResourceAsStream(basePath + simpleName);

                if (resourceAsStream == null) {
                    log.warn("Не знайдено XSD у classpath: {}", simpleName);
                    return null;
                }

                org.w3c.dom.ls.DOMImplementationLS impl =
                        (org.w3c.dom.ls.DOMImplementationLS) org.w3c.dom.bootstrap.DOMImplementationRegistry
                                .newInstance().getDOMImplementation("LS");

                org.w3c.dom.ls.LSInput input = impl.createLSInput();
                input.setPublicId(publicId);
                input.setSystemId(systemId);
                input.setByteStream(resourceAsStream);
                return input;

            } catch (Exception e) {
                log.error("Помилка при резолві XSD: {}", e.getMessage());
                return null;
            }
        });

        URL url = getClass().getResource("/" + schemaFileName);
        return sf.newSchema(url);
    }


    private boolean validate(Validator validator, String xmlFileName) {
        try (InputStream resourceAsStream = getClass().getResourceAsStream("/" + xmlFileName)){
            Source source = new StreamSource(
                    resourceAsStream);
            validator.validate(source);
            System.out.println(xmlFileName + " is valid.");}
        catch (IOException | SAXException ex) {
            log.error("{} is not valid", xmlFileName);
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "XML Validation Error: " + ex.getMessage());
        }
        return true;
    }

    private <T> T mapXmlToObject(String xmlFileName, Class<T> entityClass){
        try (InputStream xmlStream = getClass().getResourceAsStream("/" + xmlFileName)){
            JAXBContext context = JAXBContext.newInstance(entityClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(xmlStream);

            if (entityClass.isInstance(obj)) {
                return entityClass.cast(obj);
            } else {
                log.error("Unmarshalled object is not of type {}", entityClass.getSimpleName());
                return null;
            }
        } catch (IOException  | JAXBException ex) {
            log.error("{} - is not valid because of: {}", xmlFileName, ex.getMessage());
        }
        return null;
    }


}
