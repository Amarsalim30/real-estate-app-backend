package com.amarsalimprojects.real_estate_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amarsalimprojects.real_estate_app.model.MpesaPayment;

public interface MpesaPaymentRepository extends JpaRepository<MpesaPayment, Long> {

    Optional<MpesaPayment> findByCheckoutRequestId(String checkoutRequestId);

    List<MpesaPayment> findByInvoiceIdOrderByCreatedAtDesc(Long invoiceId);

    List<MpesaPayment> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
}
