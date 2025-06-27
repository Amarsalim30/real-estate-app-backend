package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarsalimprojects.real_estate_app.dto.PaymentStatisticsDTO;
import com.amarsalimprojects.real_estate_app.dto.PaymentSummaryDTO;
import com.amarsalimprojects.real_estate_app.dto.ProcessPaymentRequest;
import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.model.BuyerProfile;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BuyerProfileRepository buyerProfileRepository;

    public Payment processPayment(ProcessPaymentRequest request) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(request.getInvoiceId());
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(request.getBuyerId());

        if (invoiceOpt.isPresent() && buyerOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            BuyerProfile buyer = buyerOpt.get();

            // Create payment
            Payment payment = Payment.builder()
                    .amount(request.getAmount())
                    .method(request.getMethod())
                    .status(PaymentStatus.PENDING)
                    .invoice(invoice)
                    .buyer(buyer)
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            // Update invoice status based on total payments
            updateInvoiceStatus(invoice);

            return savedPayment;
        } else {
            throw new IllegalArgumentException("Invoice or Buyer not found");
        }
    }

    public void updateInvoiceStatus(Invoice invoice) {
        BigDecimal totalPaid = calculateTotalPaidForInvoice(invoice.getId());

        if (totalPaid.compareTo(invoice.getTotalAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PENDING);
        }

        invoiceRepository.save(invoice);
    }

    public BigDecimal calculateTotalPaidForInvoice(Long invoiceId) {
        List<Payment> payments = paymentRepository.findByInvoiceIdAndStatus(invoiceId, PaymentStatus.COMPLETED);
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PaymentStatisticsDTO getPaymentStatistics() {
        long totalPayments = paymentRepository.count();
        long completedPayments = paymentRepository.countByStatus(PaymentStatus.COMPLETED);
        long pendingPayments = paymentRepository.countByStatus(PaymentStatus.PENDING);
        long failedPayments = paymentRepository.countByStatus(PaymentStatus.FAILED);

        BigDecimal totalAmount = paymentRepository.getTotalAmountByStatus(PaymentStatus.COMPLETED);
        BigDecimal pendingAmount = paymentRepository.getTotalAmountByStatus(PaymentStatus.PENDING);

        return PaymentStatisticsDTO.builder()
                .totalPayments(totalPayments)
                .completedPayments(completedPayments)
                .pendingPayments(pendingPayments)
                .failedPayments(failedPayments)
                .totalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO)
                .pendingAmount(pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
                .build();
    }

    public PaymentSummaryDTO getPaymentSummaryByInvoice(Long invoiceId) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            List<Payment> payments = paymentRepository.findByInvoiceId(invoiceId);

            BigDecimal totalPaid = payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remainingAmount = invoice.getTotalAmount().subtract(totalPaid);

            return PaymentSummaryDTO.builder()
                    .invoiceId(invoiceId)
                    .totalInvoiceAmount(invoice.getTotalAmount())
                    .totalPaidAmount(totalPaid)
                    .remainingAmount(remainingAmount)
                    .payments(payments)
                    .build();
        } else {
            throw new IllegalArgumentException("Invoice not found");
        }
    }

    public List<Payment> bulkUpdateStatus(List<Long> paymentIds, PaymentStatus status) {
        return paymentIds.stream()
                .map(id -> paymentRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(payment -> payment.setStatus(status))
                .map(paymentRepository::save)
                .collect(Collectors.toList());
    }

    public boolean validatePaymentAmount(Long invoiceId, BigDecimal paymentAmount) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            BigDecimal totalPaid = calculateTotalPaidForInvoice(invoiceId);
            BigDecimal remainingAmount = invoice.getTotalAmount().subtract(totalPaid);

            return paymentAmount.compareTo(remainingAmount) <= 0 && paymentAmount.compareTo(BigDecimal.ZERO) > 0;
        }

        return false;
    }

    public List<Payment> getOverduePayments() {
        // Logic to find overdue payments based on invoice due dates
        return paymentRepository.findByStatus(PaymentStatus.PENDING);
    }

    public BigDecimal getTotalRevenueByProject(Long projectId) {
        List<Payment> payments = paymentRepository.findByProjectId(projectId);
        return payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
