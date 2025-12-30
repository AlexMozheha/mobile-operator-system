package com.operator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.operator.clients.billing.dto.InvoiceCreateRequest;
import com.operator.clients.billing.dto.InvoiceDto;
import com.operator.clients.billing.dto.TariffDto;
import com.operator.clients.crm.dto.CustomerDto;
import com.operator.clients.crm.dto.TariffChangeCommand;
import com.operator.dto.CustomerProfileDto;
import com.operator.service.PortalService;
import com.operator.validation.JsonAgainstSchemaValidator;
import com.operator.validation.XmlAgainstSchemaValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portal")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PortalController {
    private final PortalService portalService;

    private final JsonAgainstSchemaValidator jsonValidator;
    private final XmlAgainstSchemaValidator xmlValidator;

    private final ObjectMapper jsonMapper;
    private final XmlMapper xmlMapper;


    @GetMapping("/profile/{id}")
    public ResponseEntity<CustomerProfileDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(portalService.getFullProfile(id));
    }

    @PostMapping("/change-tariff")
    public ResponseEntity<Void> changeTariff(@RequestBody TariffChangeCommand command) {
        portalService.changeTariff(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/customer")
    public ResponseEntity<Page<CustomerDto>> listCustomers(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(portalService.getAllCustomers(page, size));
    }

    @GetMapping("/admin/tariff")
    public ResponseEntity<Page<TariffDto>> listTariffs(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(portalService.getAllTariffs(page, size));
    }



    @PostMapping (value = "/invoice/create", consumes = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody String body, HttpServletRequest request) throws Exception {
        String contentType = request.getContentType();

        String cleanBody = body.trim();
        if (cleanBody.startsWith("\"") && cleanBody.endsWith("\"")) {
            cleanBody = jsonMapper.readValue(cleanBody, String.class);
        }

        InvoiceCreateRequest invoiceCreateRequest;
        if (contentType != null && contentType.contains("xml")) {
            xmlValidator.validateString(cleanBody, "xml/invoice.xsd");
            invoiceCreateRequest = xmlMapper.readValue(cleanBody, InvoiceCreateRequest.class);
        } else {
            jsonValidator.validateString(cleanBody, "json/invoice-schema.json");
            invoiceCreateRequest = jsonMapper.readValue(cleanBody, InvoiceCreateRequest.class);
        }

        return ResponseEntity.ok(portalService.createInvoice(invoiceCreateRequest));
    }

    @PostMapping("/invoice/{id}/pay")
    public ResponseEntity<Void> payInvoice(@PathVariable Long id) throws Exception {
        portalService.payInvoice(id);
        return ResponseEntity.ok().build();
    }
}