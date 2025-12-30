package com.operator.service;

import com.operator.clients.billing.BillingClient;
import com.operator.clients.billing.dto.InvoiceCreateRequest;
import com.operator.clients.billing.dto.InvoiceDto;
import com.operator.clients.billing.dto.TariffDto;
import com.operator.clients.crm.CrmClient;
import com.operator.clients.crm.dto.CustomerDto;
import com.operator.clients.crm.dto.TariffChangeCommand;
import com.operator.dto.CustomerProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortalService {
    private final CrmClient crmClient;
    private final BillingClient billingClient;

    public CustomerProfileDto getFullProfile(Long customerId) {
        var customer = crmClient.getCustomerById(customerId);
        if (customer == null) {
            throw new RuntimeException("Customer not found in CRM");
        }

        var balance = billingClient.getBalanceByCustomerId(customerId);
        var tariff = (customer.tariffId() != null) ? billingClient.getTariffById(customer.tariffId()) : null;
        var usage = billingClient.getUsageByCustomerId(customerId);
        var invoices = billingClient.getInvoicesByCustomerId(customerId);

        return new CustomerProfileDto(customer, balance, tariff, usage, invoices);
    }

    public Page<CustomerDto> getAllCustomers(int page, int size) {
        return crmClient.getAllCustomers(page, size);
    }

    public Page<TariffDto> getAllTariffs(int page, int size) {
        return billingClient.getAllTariffs(page, size);
    }

    public void changeTariff(TariffChangeCommand command) {
        billingClient.changeCustomerTariff(command);
    }

    public InvoiceDto createInvoice(InvoiceCreateRequest invoiceCreateRequest) {
        return billingClient.createInvoice(invoiceCreateRequest);
    }

    public void payInvoice(long invoiceId) {
        billingClient.payInvoice(invoiceId);
    }

}
