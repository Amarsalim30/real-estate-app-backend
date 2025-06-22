package com.amarsalimprojects.real_estate_app.service;

import com.amarsalimprojects.real_estate_app.dto.*;
import com.amarsalimprojects.real_estate_app.model.*;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private BuyerProfileRepository buyerProfileRepository;

    public PaymentDetail createBankPaymentDetail(CreateBankPaymentRequest request) {
        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(request.getInvoiceId());
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(request.getBuyerId());

        if (paymentOpt.isPresent() && invoiceOpt.isPresent() && buyerOpt.isPresent()) {
            BankDetails bankDetails = new BankDetails(
                    request.getBankName(),
                    request.getAccountLast4(),
                    request.getRoutingNumber()
            );

            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .amount(request.getAmount())
                    .status(PaymentStatus.PENDING)
                    .method(PaymentMethod.BANK_TRANSFER)
                    .transactionId(request.getTransactionId())
                    .payment(paymentOpt.get())
                    .invoice(invoiceOpt.get())
                    .buyer(buyerOpt.get())
                    .bankDetails(bankDetails)
                    .build();

            return paymentDetailRepository.save(paymentDetail);
        } else {
            throw new IllegalArgumentException("Payment, Invoice, or Buyer not found");
        }
    }

    public PaymentDetail createCardPaymentDetail(CreateCardPaymentRequest request) {
        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(request.getInvoiceId());
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(request.getBuyerId());

        if (paymentOpt.isPresent() && invoiceOpt.isPresent() && buyerOpt.isPresent()) {
            CardDetails cardDetails = new CardDetails(
                    request.getLastFour(),
                    request.getBrand(),
                    request.getExpiryMonth(),
                    request.getExpiryYear()
            );

            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .amount(request.getAmount())
                    .status(PaymentStatus.PENDING)
                    .method(PaymentMethod.CREDIT_CARD)
                    .transactionId(request.getTransactionId())
                    .payment(paymentOpt.get())
                    .invoice(invoiceOpt.get())
                    .buyer(buyerOpt.get())
                    .cardDetails(cardDetails)
                    .build();

            return paymentDetailRepository.save(paymentDetail);
        } else {
            throw new IllegalArgumentException("Payment, Invoice, or Buyer not found");
        }
    }

    public PaymentDetail createCheckPaymentDetail(CreateCheckPaymentRequest request) {
        Optional<Payment> paymentOpt = paymentRepository.findById(request.getPaymentId());
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(request.getInvoiceId());
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(request.getBuyerId());

        if (paymentOpt.isPresent() && invoiceOpt.isPresent() && buyerOpt.isPresent()) {
            CheckDetails checkDetails = new CheckDetails(
                    request.getCheckNumber(),
                    request.getBankName(),
                    request.getAccountNumber()
            );

            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .amount(request.getAmount())
                    .status(PaymentStatus.PENDING)
                    .method(PaymentMethod.CHECK)
                    .transactionId(request.getTransactionId())
                    .payment(paymentOpt.get())
                    .invoice(invoiceOpt.get())
                    .buyer(buyerOpt.get())
                    .checkDetails(checkDetails)
                    .build();

            return paymentDetailRepository.save(paymentDetail);
        } else {
            throw new IllegalArgumentException("Payment, Invoice, or Buyer not found");
        }
    }

    public PaymentDetailStatisticsDTO getPaymentDetailStatistics() {
        long totalPaymentDetails = paymentDetailRepository.count();
        long completedPaymentDetails = paymentDetailRepository.countByStatus(PaymentStatus.COMPLETED);
        long pendingPaymentDetails = paymentDetailRepository.countByStatus(PaymentStatus.PENDING);
        long failedPaymentDetails = paymentDetailRepository.countByStatus(PaymentStatus.FAILED);

        BigDecimal totalAmount = paymentDetailRepository.getTotalAmountByStatus(PaymentStatus.COMPLETED);
        BigDecimal pendingAmount = paymentDetailRepository.getTotalAmountByStatus(PaymentStatus.PENDING);

        return PaymentDetailStatisticsDTO.builder()
                .totalPaymentDetails(totalPaymentDetails)
                .completedPaymentDetails(completedPaymentDetails)
                .pendingPaymentDetails(pendingPaymentDetails)
                .failedPaymentDetails(failedPaymentDetails)
                .totalAmountProcessed(totalAmount != null ? totalAmount : BigDecimal.ZERO)
                .pendingAmount(pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
                .build();
    }

    public List<PaymentMethodBreakdownDTO> getPaymentMethodBreakdown() {
        List<Object[]> results = paymentDetailRepository.getPaymentMethodBreakdown(PaymentStatus.COMPLETED);

        return results.stream()
                .map(result -> PaymentMethodBreakdownDTO.builder()
                .paymentMethod((PaymentMethod) result[0])
                .count((Long) result[1])
                .totalAmount((BigDecimal) result[2])
                .build())
                .collect(Collectors.toList());
    }

    public List<PaymentDetail> bulkUpdateStatus(List<Long> paymentDetailIds, PaymentStatus status) {
        return paymentDetailIds.stream()
                .map(id -> paymentDetailRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(paymentDetail -> paymentDetail.setStatus(status))
                .map(paymentDetailRepository::save)
                .collect(Collectors.toList());
    }

    public PaymentDetail retryPaymentDetail(Long paymentDetailId) {
        Optional<PaymentDetail> paymentDetailOpt = paymentDetailRepository.findById(paymentDetailId);

        if (paymentDetailOpt.isPresent()) {
            PaymentDetail paymentDetail = paymentDetailOpt.get();

            // Only retry if status is FAILED
            if (paymentDetail.getStatus() == PaymentStatus.FAILED) {
                paymentDetail.setStatus(PaymentStatus.PROCESSING);
                PaymentDetail savedPaymentDetail = paymentDetailRepository.save(paymentDetail);

                // Here you would integrate with actual payment processing logic
                // For now, we'll simulate processing
                processPaymentDetail(savedPaymentDetail);

                return savedPaymentDetail;
            } else {
                throw new IllegalStateException("Can only retry failed payment details");
            }
        } else {
            throw new IllegalArgumentException("Payment detail not found");
        }
    }

    private void processPaymentDetail(PaymentDetail paymentDetail) {
        // Simulate payment processing logic
        // In a real application, this would integrate with payment gateways
        try {
            // Simulate processing delay
            Thread.sleep(1000);

            // Simulate success/failure (90% success rate)
            if (Math.random() < 0.9) {
                paymentDetail.setStatus(PaymentStatus.COMPLETED);
            } else {
                paymentDetail.setStatus(PaymentStatus.FAILED);
            }

            paymentDetailRepository.save(paymentDetail);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            paymentDetail.setStatus(PaymentStatus.FAILED);
            paymentDetailRepository.save(paymentDetail);
        }
    }

    public List<PaymentDetailAuditDTO> getPaymentDetailAuditTrail(Long paymentDetailId) {
        // This would typically come from an audit table
        // For now, we'll return basic information
        Optional<PaymentDetail> paymentDetailOpt = paymentDetailRepository.findById(paymentDetailId);

        if (paymentDetailOpt.isPresent()) {
            PaymentDetail paymentDetail = paymentDetailOpt.get();

            return List.of(
                    PaymentDetailAuditDTO.builder()
                            .paymentDetailId(paymentDetailId)
                            .action("CREATED")
                            .timestamp(paymentDetail.getCreatedAt())
                            .status(paymentDetail.getStatus())
                            .amount(paymentDetail.getAmount())
                            .build(),
                    PaymentDetailAuditDTO.builder()
                            .paymentDetailId(paymentDetailId)
                            .action("LAST_UPDATED")
                            .timestamp(paymentDetail.getUpdatedAt())
                            .status(paymentDetail.getStatus())
                            .amount(paymentDetail.getAmount())
                            .build()
            );
        } else {
            throw new IllegalArgumentException("Payment detail not found");
        }
    }

    public boolean validatePaymentDetailAmount(Long invoiceId, BigDecimal amount) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);

        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            List<PaymentDetail> existingPaymentDetails = paymentDetailRepository.findByInvoiceIdAndStatus(invoiceId, PaymentStatus.COMPLETED);

            BigDecimal totalPaid = existingPaymentDetails.stream()
                    .map(PaymentDetail::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal remainingAmount = invoice.getTotalAmount().subtract(totalPaid);

            return amount.compareTo(remainingAmount) <= 0 && amount.compareTo(BigDecimal.ZERO) > 0;
        }

        return false;
    }

    public BigDecimal getTotalProcessedAmountByProject(Long projectId) {
        List<PaymentDetail> paymentDetails = paymentDetailRepository.findByProjectId(projectId);
        return paymentDetails.stream()
                .filter(pd -> pd.getStatus() == PaymentStatus.COMPLETED)
                .map(PaymentDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<PaymentDetail> getPaymentDetailsByBuyerAndDateRange(Long buyerId, LocalDateTime startDate, LocalDateTime endDate) {
        return paymentDetailRepository.findByBuyerId(buyerId).stream()
                .filter(pd -> pd.getCreatedAt().isAfter(startDate) && pd.getCreatedAt().isBefore(endDate))
                .collect(Collectors.toList());
    }
}
