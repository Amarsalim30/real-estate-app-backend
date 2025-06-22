package com.amarsalimprojects.real_estate_app.components;

import com.amarsalimprojects.real_estate_app.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvoiceScheduler {

    @Autowired
    private InvoiceService invoiceService;

    @Scheduled(cron = "0 0 9 * * ?") // Daily at 9 AM
    public void checkOverdueInvoices() {
        invoiceService.markOverdueInvoices();
    }

    @Scheduled(cron = "0 0 10 1 * ?") // Monthly on 1st at 10 AM
    public void generateMonthlyInvoiceReport() {
        invoiceService.generateMonthlyReport();
    }
}
