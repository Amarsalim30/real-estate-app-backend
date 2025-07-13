package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarsalimprojects.real_estate_app.dto.requests.PurchaseUnitRequest;
import com.amarsalimprojects.real_estate_app.dto.responses.PurchaseUnitResponse;
import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.BuyerProfile;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.MpesaPayment;
import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.model.PaymentPlan;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.MpesaPaymentRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentDetailRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentPlanRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    @Autowired
    private MpesaPaymentRepository mpesaPaymentRepository;

    private final UnitRepository unitRepository;
    private final BuyerProfileRepository buyerProfileRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final MpesaStkService mpesaStkService;

    public PurchaseUnitResponse handlePurchase(Long unitId, PurchaseUnitRequest dto) {
        // Validate buyer
        BuyerProfile buyer = buyerProfileRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found"));

        // Validate unit
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        if (unit.getStatus() != UnitStatus.AVAILABLE) {
            throw new IllegalStateException("Unit is not available for purchase");
        }

        // Validate payment plan if provided
        PaymentPlan plan = null;
        if (dto.getPaymentPlanId() != null) {
            plan = paymentPlanRepository.findById(dto.getPaymentPlanId())
                    .orElseThrow(() -> new EntityNotFoundException("Payment plan not found"));
        }

        // Create invoice
        Invoice invoice = Invoice.builder()
                .unit(unit)
                .buyer(buyer)
                .paymentPlan(plan)
                .totalAmount(dto.getTotalAmount() != null ? dto.getTotalAmount() : unit.getPrice())
                .status(InvoiceStatus.PENDING)
                .issuedDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(7))
                .build();
        invoice = invoiceRepository.save(invoice);

        String paddedId = String.format("%05d", invoice.getId());
        String year = String.valueOf(LocalDate.now().getYear());
        String invoiceNumber = "INV-" + year + "-" + paddedId;

        invoice.setInvoiceNumber(invoiceNumber);

        invoice = invoiceRepository.save(invoice);
        // Create initial payment record
        Payment payment = Payment.builder()
                .invoice(invoice)
                .buyer(buyer)
                .amount(dto.getDownPaymentAmount() != null ? dto.getDownPaymentAmount() : unit.getPrice())
                .method(PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()))
                .status(PaymentStatus.PENDING)
                .paymentDate(LocalDate.now())
                .build();

        payment = paymentRepository.save(payment);

        String message;
        PaymentMethod paymentMethod = PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase());

        switch (paymentMethod) {
            case MPESA_STKPUSH -> {
                try {
                    // Trigger STK Push
                    var stkResponse = mpesaStkService.initiateStkPush(
                            dto.getMpesaNumber(),
                            payment.getAmount(),
                            invoice.getId()
                    );
                    invoice.setCheckoutRequestId(stkResponse.getCheckoutRequestId());
                    invoiceRepository.save(invoice); // update with requestId

                    message = "STK Push sent to " + dto.getMpesaNumber() + ". " + stkResponse.getCustomerMessage();
                } catch (Exception e) {
                    message = "Failed to initiate STK Push: " + e.getMessage();
                    payment.setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
            }
            // case BANK_TRANSFER -> {
            //     if (dto.getBankDetails() != null) {
            //         PaymentDetail detail = PaymentDetail.builder()
            //                 .payment(payment)
            //                 .invoice(invoice)
            //                 .buyer(buyer)
            //                 .method(PaymentMethod.WIRE_TRANSFER)
            //                 .bankName(dto.getBankDetails().getBankName())
            //                 .bankAccountNumber(dto.getBankDetails().getAccountNumber())
            //                 .bankRoutingNumber(dto.getBankDetails().getRoutingNumber())
            //                 .build();

            //         paymentDetailRepository.save(detail);
            //     }
            //     message = "Bank details submitted. Awaiting confirmation.";
            // }
            case MPESA_PAYBILL -> {
                // No STK Push; user is instructed to pay via M-Pesa manually
                message = "Use Paybill 123456, Account: UNIT-" + unit.getId();
            }
            default ->
                throw new IllegalArgumentException("Unsupported payment method: " + dto.getPaymentMethod());
        }

        // // Mark unit as RESERVED
        // unit.setStatus(UnitStatus.RESERVED);
        // unit.setBuyer(buyer); // Associate buyer with unit
        // unitRepository.save(unit);
        return PurchaseUnitResponse.builder()
                .invoiceId(invoice.getId())
                .unitId(unit.getId())
                .buyerId(buyer.getId())
                .unitName(unit.getUnitNumber())
                .projectName(unit.getProject() != null ? unit.getProject().getName() : "N/A")
                .amount(invoice.getTotalAmount())
                .paymentMethod(payment.getMethod().toString())
                .invoiceStatus(invoice.getStatus().toString())
                .paymentStatus(payment.getStatus().toString())
                .message(message)
                .build();
    }

    @Transactional
    public void handleSuccessfulPayment(String checkoutRequestId) {
        try {
            // 1. Find MpesaPayment by checkoutRequestId
            Optional<MpesaPayment> optionalMpesaPayment = mpesaPaymentRepository.findByCheckoutRequestId(checkoutRequestId);

            if (optionalMpesaPayment.isEmpty()) {
                throw new EntityNotFoundException("M-Pesa payment not found for checkout request ID: " + checkoutRequestId);
            }

            MpesaPayment mpesaPayment = optionalMpesaPayment.get();

            // 2. Get Invoice from MpesaPayment
            Invoice invoice = mpesaPayment.getInvoice();
            if (invoice == null) {
                throw new EntityNotFoundException("Invoice not found for M-Pesa payment");
            }

            // 3. Get Unit from Invoice
            Unit unit = invoice.getUnit();
            if (unit == null) {
                throw new EntityNotFoundException("Unit not found for invoice");
            }

            // 4. Get BuyerProfile from Invoice
            BuyerProfile buyerProfile = invoice.getBuyer();
            if (buyerProfile == null) {
                throw new EntityNotFoundException("Buyer profile not found for invoice");
            }

            // 🔹 Step 1: Always reserve the unit if it's not yet reserved/sold
            if (unit.getStatus() == UnitStatus.AVAILABLE) {
                unit.setStatus(UnitStatus.RESERVED);
                unit.setBuyer(buyerProfile);
                unitRepository.save(unit);
            }

            // 🔹 Step 2: Check if full payment is now completed
            BigDecimal totalPaid = invoice.getTotalMpesaPayments();
            BigDecimal totalRequired = invoice.getTotalAmount();

            if (totalPaid.compareTo(totalRequired) >= 0) {
                // Mark invoice as PAID
                invoice.setStatus(InvoiceStatus.PAID);
                invoiceRepository.save(invoice);

                // Mark unit as SOLD if not already
                if (unit.getStatus() != UnitStatus.SOLD) {
                    unit.setStatus(UnitStatus.SOLD);
                    unitRepository.save(unit);
                }
            } else {
                // Handle partial payment - mark invoice as PARTIALLY_PAID
                invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
                invoiceRepository.save(invoice);

                // Keep unit as RESERVED for partial payments
                if (unit.getStatus() == UnitStatus.AVAILABLE) {
                    unit.setStatus(UnitStatus.RESERVED);
                    unit.setBuyer(buyerProfile);
                    unitRepository.save(unit);
                }
            }

            // 7. Create Payment record for tracking
            Payment payment = new Payment();
            payment.setAmount(mpesaPayment.getAmount());
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setMethod(PaymentMethod.MPESA_STKPUSH);
            payment.setInvoice(invoice);
            payment.setBuyer(buyerProfile);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            // Optional: Create PaymentDetail record if you have that entity
            // PaymentDetail paymentDetail = new PaymentDetail();
            // paymentDetail.setPayment(payment);
            // paymentDetail.setMpesaReceiptNumber(mpesaPayment.getMpesaReceiptNumber());
            // paymentDetail.setTransactionDate(mpesaPayment.getTransactionDate());
            // paymentDetailRepository.save(paymentDetail);
        } catch (Exception e) {
            throw new RuntimeException("Failed to handle successful payment: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handlePartialPayment(String checkoutRequestId) {
        try {
            Optional<MpesaPayment> optionalMpesaPayment = mpesaPaymentRepository.findByCheckoutRequestId(checkoutRequestId);

            if (optionalMpesaPayment.isEmpty()) {
                throw new EntityNotFoundException("M-Pesa payment not found for checkout request ID: " + checkoutRequestId);
            }

            MpesaPayment mpesaPayment = optionalMpesaPayment.get();
            Invoice invoice = mpesaPayment.getInvoice();
            Unit unit = invoice.getUnit();
            BuyerProfile buyerProfile = invoice.getBuyer();

            // Reserve unit for partial payment
            if (unit != null && unit.getStatus() == UnitStatus.AVAILABLE) {
                unit.setStatus(UnitStatus.RESERVED);
                unit.setBuyer(buyerProfile);
                unitRepository.save(unit);
            }

            // Mark invoice as partially paid
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
            invoiceRepository.save(invoice);

            // Create payment record
            Payment payment = new Payment();
            payment.setAmount(mpesaPayment.getAmount());
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setMethod(PaymentMethod.MPESA_STKPUSH);
            payment.setInvoice(invoice);
            payment.setBuyer(buyerProfile);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Failed to handle partial payment: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void checkAndUpdatePaymentStatus(Invoice invoice) {
        if (invoice == null) {
            return;
        }

        BigDecimal totalPaid = invoice.getTotalMpesaPayments();
        BigDecimal totalRequired = invoice.getTotalAmount();
        Unit unit = invoice.getUnit();

        if (totalPaid.compareTo(totalRequired) >= 0) {
            // Full payment completed
            invoice.setStatus(InvoiceStatus.PAID);
            invoiceRepository.save(invoice);

            if (unit != null && unit.getStatus() != UnitStatus.SOLD) {
                unit.setStatus(UnitStatus.SOLD);
                unitRepository.save(unit);
            }
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            // Partial payment
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
            invoiceRepository.save(invoice);

            if (unit != null && unit.getStatus() == UnitStatus.AVAILABLE) {
                unit.setStatus(UnitStatus.RESERVED);
                unit.setBuyer(invoice.getBuyer());
                unitRepository.save(unit);
            }
        }
    }

}
