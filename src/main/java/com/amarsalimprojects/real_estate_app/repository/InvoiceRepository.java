package com.amarsalimprojects.real_estate_app.repository;

import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByStatus(InvoiceStatus status);

    List<Invoice> findByBuyerId(Long buyerId);

    List<Invoice> findByUnitId(Long unitId);

    List<Invoice> findByProjectId(Long projectId);

    List<Invoice> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);

    List<Invoice> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    List<Invoice> findByDueDateBeforeAndStatus(LocalDate date, InvoiceStatus status);

    List<Invoice> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    @Query("SELECT i FROM Invoice i WHERE i.dueDate < :currentDate AND i.status = :status")
    List<Invoice> findOverdueInvoices(@Param("currentDate") LocalDate currentDate, @Param("status") InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = :status")
    BigDecimal getTotalAmountByStatus(@Param("status") InvoiceStatus status);
}
