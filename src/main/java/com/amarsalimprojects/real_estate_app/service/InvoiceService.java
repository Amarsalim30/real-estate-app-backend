package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.model.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UnitRepository unitRepository;

    public Invoice createInvoiceForUnit(Long unitId, Long buyerId) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found"));

        // Check if invoice already exists for this unit
        Optional<Invoice> existingInvoice = invoiceRepository.findByUnitId(unitId);
        if (existingInvoice.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invoice already exists for this unit");
        }

        Invoice invoice = Invoice.builder()
                .unit(unit)
                .buyer(unit.getBuyer())
                .totalAmount(unit.getPrice())
                .status(InvoiceStatus.PENDING)
                .issuedDate(LocalDate.now())
                .build();

        return invoiceRepository.save(invoice);
    }

    public void processPayment(Long invoiceId, BigDecimal paymentAmount) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invoice is already paid");
        }

        // Calculate remaining amount after payments
        BigDecimal totalPaid = calculateTotalPaid(invoice);
        BigDecimal remainingAmount = invoice.getTotalAmount().subtract(totalPaid);

        if (paymentAmount.compareTo(remainingAmount) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);
    }

    private BigDecimal calculateTotalPaid(Invoice invoice) {
        return invoice.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markOverdueInvoices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void generateMonthlyReport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
