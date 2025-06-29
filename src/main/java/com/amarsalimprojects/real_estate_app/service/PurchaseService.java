package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.amarsalimprojects.real_estate_app.dto.requests.PurchaseUnitRequest;
import com.amarsalimprojects.real_estate_app.dto.responses.PurchaseUnitResponse;
import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.BuyerProfile;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.model.PaymentDetail;
import com.amarsalimprojects.real_estate_app.model.PaymentPlan;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentDetailRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentPlanRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

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

        // Mark unit as RESERVED
        unit.setStatus(UnitStatus.RESERVED);
        unit.setBuyer(buyer); // Associate buyer with unit
        unitRepository.save(unit);

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
}
