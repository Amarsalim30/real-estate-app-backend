package com.amarsalimprojects.real_estate_app.repository;

import com.amarsalimprojects.real_estate_app.model.PaymentDetail;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    Optional<PaymentDetail> findByPaymentNumber(String paymentNumber);

    Optional<PaymentDetail> findByTransactionId(String transactionId);

    List<PaymentDetail> findByStatus(PaymentStatus status);

    List<PaymentDetail> findByPaymentMethod(PaymentMethod paymentMethod);

    List<PaymentDetail> findByBuyerId(Long buyerId);

    List<PaymentDetail> findByInvoiceId(Long invoiceId);

    List<PaymentDetail> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<PaymentDetail> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<PaymentDetail> findByStatusAndPaymentDateBetween(PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);

    List<PaymentDetail> findByBuyerIdAndStatus(Long buyerId, PaymentStatus status);

    List<PaymentDetail> findByInvoiceIdAndStatus(Long invoiceId, PaymentStatus status);

    List<PaymentDetail> findByPaymentMethodAndStatus(PaymentMethod paymentMethod, PaymentStatus status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.processingFee > :fee")
    List<PaymentDetail> findByProcessingFeeGreaterThan(@Param("fee") BigDecimal fee);

    @Query("SELECT SUM(pd.amount) FROM PaymentDetail pd WHERE pd.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(pd.processingFee) FROM PaymentDetail pd WHERE pd.status = :status")
    BigDecimal getTotalProcessingFeeByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.paymentDate BETWEEN :startDate AND :endDate")
    List<PaymentDetail> findPaymentDetailsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.bankDetails.bankName = :bankName")
    List<PaymentDetail> findByBankName(@Param("bankName") String bankName);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.cardDetails.brand = :brand")
    List<PaymentDetail> findByCardBrand(@Param("brand") String brand);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.checkDetails.checkNumber = :checkNumber")
    Optional<PaymentDetail> findByCheckNumber(@Param("checkNumber") String checkNumber);

    @Query("SELECT COUNT(pd) FROM PaymentDetail pd WHERE pd.paymentMethod = :method AND pd.status = :status")
    Long countByPaymentMethodAndStatus(@Param("method") PaymentMethod method, @Param("status") PaymentStatus status);
}
