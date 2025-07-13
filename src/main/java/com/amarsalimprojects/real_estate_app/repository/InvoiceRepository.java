package com.amarsalimprojects.real_estate_app.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByStatus(InvoiceStatus status);

// ✅ Instead, use:
    @Query("SELECT i FROM Invoice i WHERE i.buyer.id = :buyerId")
    List<Invoice> findByBuyer_Id(@Param("buyerId") Long buyerId);

    @Query("SELECT i FROM Invoice i WHERE i.unit.id = :unitId")
    Optional<Invoice> findInvoiceByUnitId(@Param("unitId") Long unitId);

    List<Invoice> findByIssuedDateBetween(LocalDate startDate, LocalDate endDate);

    List<Invoice> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT i FROM Invoice i WHERE i.unit.project.id = :projectId")
    List<Invoice> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = :status")
    Long countByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT i FROM Invoice i WHERE i.totalAmount > :amount")
    List<Invoice> findByTotalAmountGreaterThan(@Param("amount") BigDecimal amount);

    @Query("SELECT i FROM Invoice i WHERE i.issuedDate >= :date")
    List<Invoice> findByIssuedDateAfter(@Param("date") LocalDate date);

    @Query("SELECT i FROM Invoice i WHERE i.buyer.email = :email")
    List<Invoice> findByBuyerEmail(@Param("email") String email);

    Optional<Invoice> findByCheckoutRequestId(String id);

}
