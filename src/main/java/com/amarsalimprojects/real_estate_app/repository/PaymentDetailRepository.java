package com.amarsalimprojects.real_estate_app.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.model.PaymentDetail;

@Repository
public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Long> {

    Optional<PaymentDetail> findByTransactionId(String transactionId);

    List<PaymentDetail> findByStatus(PaymentStatus status);

    List<PaymentDetail> findByMethod(PaymentMethod method);

    List<PaymentDetail> findByBuyerId(Long buyerId);

    List<PaymentDetail> findByInvoiceId(Long invoiceId);

    List<PaymentDetail> findByPaymentId(Long paymentId);

    List<PaymentDetail> findByBuyerIdAndStatus(Long buyerId, PaymentStatus status);

    List<PaymentDetail> findByInvoiceIdAndStatus(Long invoiceId, PaymentStatus status);

    List<PaymentDetail> findByMethodAndStatus(PaymentMethod method, PaymentStatus status);

    List<PaymentDetail> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<PaymentDetail> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<PaymentDetail> findByStatusAndCreatedAtBetween(PaymentStatus status, LocalDateTime startDate, LocalDateTime endDate);

    // Queries for embedded objects
    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.bankDetails.bankName = :bankName")
    List<PaymentDetail> findByBankDetailsBankName(@Param("bankName") String bankName);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.cardDetails.brand = :brand")
    List<PaymentDetail> findByCardDetailsBrand(@Param("brand") String brand);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.checkDetails.checkNumber = :checkNumber")
    Optional<PaymentDetail> findByCheckDetailsCheckNumber(@Param("checkNumber") String checkNumber);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.bankDetails.accountLast4 = :accountLast4")
    List<PaymentDetail> findByBankDetailsAccountLast4(@Param("accountLast4") String accountLast4);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.cardDetails.lastFour = :lastFour")
    List<PaymentDetail> findByCardDetailsLastFour(@Param("lastFour") String lastFour);

    // Aggregate queries
    @Query("SELECT SUM(pd.amount) FROM PaymentDetail pd WHERE pd.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(pd.amount) FROM PaymentDetail pd WHERE pd.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(pd) FROM PaymentDetail pd WHERE pd.method = :method AND pd.status = :status")
    Long countByMethodAndStatus(@Param("method") PaymentMethod method, @Param("status") PaymentStatus status);

    @Query("SELECT COUNT(pd) FROM PaymentDetail pd WHERE pd.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.amount > :amount")
    List<PaymentDetail> findByAmountGreaterThan(@Param("amount") BigDecimal amount);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.buyer.email = :email")
    List<PaymentDetail> findByBuyerEmail(@Param("email") String email);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.invoice.unit.project.id = :projectId")
    List<PaymentDetail> findByProjectId(@Param("projectId") Long projectId);

    // Method breakdown queries
    @Query("SELECT pd.method, COUNT(pd), SUM(pd.amount) FROM PaymentDetail pd WHERE pd.status = :status GROUP BY pd.method")
    List<Object[]> getPaymentMethodBreakdown(@Param("status") PaymentStatus status);

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.transactionId IS NOT NULL AND pd.transactionId != ''")
    List<PaymentDetail> findAllWithTransactionId();

    @Query("SELECT pd FROM PaymentDetail pd WHERE pd.method = :method AND pd.createdAt BETWEEN :startDate AND :endDate")
    List<PaymentDetail> findByMethodAndDateRange(@Param("method") PaymentMethod method, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
