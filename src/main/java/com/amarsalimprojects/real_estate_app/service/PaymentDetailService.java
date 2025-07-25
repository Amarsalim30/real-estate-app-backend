package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarsalimprojects.real_estate_app.dto.PaymentDetailAuditDTO;
import com.amarsalimprojects.real_estate_app.dto.PaymentDetailStatisticsDTO;
import com.amarsalimprojects.real_estate_app.dto.PaymentMethodBreakdownDTO;
import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.PaymentDetail;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentDetailRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;

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
