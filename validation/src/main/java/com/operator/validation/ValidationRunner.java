package microservice.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ValidationRunner implements ApplicationRunner {

    final Environment environment;
    final JsonAgainstSchemaValidator jsonValidator;
    final XmlAgainstSchemaValidator xmlValidator;

    @Autowired
    public ValidationRunner(Environment environment, JsonAgainstSchemaValidator jsonValidator, XmlAgainstSchemaValidator xmlValidator) {
        this.environment = environment;
        this.jsonValidator = jsonValidator;
        this.xmlValidator = xmlValidator;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(environment.getProperty("spring.application.name", "Validation Application"));

        // JSON
        jsonValidator.validate("json/customer-schema.json",
                "json/customer.json", "json/customer-incorrect.json");
        jsonValidator.validate("json/balance-schema.json",
                "json/balance.json", "json/balance-incorrect.json");
        jsonValidator.validate("json/invoice-schema.json",
                "json/invoice.json", "json/invoice-incorrect.json");
        jsonValidator.validate("json/tariff-schema.json",
                "json/tariff.json", "json/tariff-incorrect.json");
        jsonValidator.validate("json/usage-record-schema.json",
                "json/usage-record.json", "json/usage-record-incorrect.json");

        // XML
        xmlValidator.validate("xml/customer.xsd",
                "xml/customer.xml", "xml/customer-incorrect.xml");
        xmlValidator.validate("xml/balance.xsd",
                "xml/balance.xml", "xml/balance-incorrect.xml");
        xmlValidator.validate("xml/invoice.xsd",
                "xml/invoice.xml", "xml/invoice-incorrect.xml");
        xmlValidator.validate("xml/tariff.xsd",
                "xml/tariff.xml", "xml/tariff-incorrect.xml");
        xmlValidator.validate("xml/usage-record.xsd",
                "xml/usage-record.xml", "xml/usage-record-incorrect.xml");
    }
}