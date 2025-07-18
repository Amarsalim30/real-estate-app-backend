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
import com.amarsalimprojects.real_estate_app.model.MpesaPayment;
import com.amarsalimprojects.real_estate_app.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByMethod(PaymentMethod method);

    @Query("SELECT p FROM Payment p WHERE p.buyer.id = :buyerId")
    List<Payment> findByBuyer_Id(@Param("buyerId") Long buyerId);

    @Query("SELECT p FROM Payment p WHERE p.invoice.id = :invoiceId")
    List<Payment> findByInvoice_Id(@Param("invoiceId") Long invoiceId);

    List<Payment> findByBuyer_IdAndStatus(Long buyerId, PaymentStatus status);

    List<Payment> findByInvoice_IdAndStatus(Long invoiceId, PaymentStatus status);

    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Payment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.amount > :amount")
    List<Payment> findByAmountGreaterThan(@Param("amount") BigDecimal amount);

    @Query("SELECT p FROM Payment p WHERE p.buyer.email = :email")
    List<Payment> findByBuyerEmail(@Param("email") String email);

    @Query("SELECT p FROM Payment p WHERE p.invoice.unit.project.id = :projectId")
    List<Payment> findByProjectId(@Param("projectId") Long projectId);

    // Add method to find MpesaPayment by checkout request ID
    @Query("SELECT mp FROM MpesaPayment mp WHERE mp.checkoutRequestId = :checkoutRequestId")
    Optional<MpesaPayment> findByCheckoutRequestId(@Param("checkoutRequestId") String checkoutRequestId);

    boolean existsByInvoice_IdAndStatus(Long invoiceId, PaymentStatus success);
}
