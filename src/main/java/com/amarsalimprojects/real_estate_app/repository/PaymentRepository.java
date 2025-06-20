package com.amarsalimprojects.real_estate_app.repository;

import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    List<Payment> findByInvoiceId(Long invoiceId);

    List<Payment> findByUnitId(Long unitId);

    List<Payment> findByBuyerId(Long buyerId);

    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<Payment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Payment> findByStatusAndPaymentDateBetween(PaymentStatus status, LocalDate startDate, LocalDate endDate);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate = :date")
    List<Payment> findPaymentsByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT p FROM Payment p WHERE p.buyer.id = :buyerId AND p.status = :status")
    List<Payment> findByBuyerIdAndStatus(@Param("buyerId") Long buyerId, @Param("status") PaymentStatus status);

    @Query("SELECT p FROM Payment p WHERE p.invoice.id = :invoiceId AND p.status = :status")
    List<Payment> findByInvoiceIdAndStatus(@Param("invoiceId") Long invoiceId, @Param("status") PaymentStatus status);
}
