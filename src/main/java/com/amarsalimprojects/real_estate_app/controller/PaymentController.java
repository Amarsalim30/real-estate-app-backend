package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amarsalimprojects.real_estate_app.dto.PaymentStatisticsDTO;
import com.amarsalimprojects.real_estate_app.dto.PaymentSummaryDTO;
import com.amarsalimprojects.real_estate_app.dto.ProcessPaymentRequest;
import com.amarsalimprojects.real_estate_app.dto.requests.MpesaCallbackRequest;
import com.amarsalimprojects.real_estate_app.dto.requests.PurchaseUnitRequest;
import com.amarsalimprojects.real_estate_app.dto.requests.StkPushRequest;
import com.amarsalimprojects.real_estate_app.dto.responses.PurchaseUnitResponse;
import com.amarsalimprojects.real_estate_app.dto.responses.StkPushResponse;
import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.MpesaPayment;
import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.MpesaPaymentRepository;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;
import com.amarsalimprojects.real_estate_app.service.InvoiceService;
import com.amarsalimprojects.real_estate_app.service.MpesaStkService;
import com.amarsalimprojects.real_estate_app.service.PaymentService;
import com.amarsalimprojects.real_estate_app.service.PurchaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private MpesaPaymentRepository mpesaPaymentRepository;

    @Autowired
    private MpesaStkService mpesaStkService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        try {
            // Set default values if not provided
            if (payment.getStatus() == null) {
                payment.setStatus(PaymentStatus.PENDING);
            }

            Payment savedPayment = paymentRepository.save(payment);
            return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all payments
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") Long id) {
        try {
            Optional<Payment> payment = paymentRepository.findById(id);
            if (payment.isPresent()) {
                return new ResponseEntity<>(payment.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<Payment> payments = paymentRepository.findByStatus(status);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by payment method
    @GetMapping("/method/{method}")
    public ResponseEntity<List<Payment>> getPaymentsByMethod(@PathVariable("method") PaymentMethod method) {
        try {
            List<Payment> payments = paymentRepository.findByMethod(method);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by invoice ID
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<Payment>> getPaymentsByInvoiceId(@PathVariable("invoiceId") Long invoiceId) {
        try {
            List<Payment> payments = paymentRepository.findByInvoiceId(invoiceId);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Payment>> getPaymentsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Payment> payments = paymentRepository.findByBuyerId(buyerId);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by buyer ID and status
    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByBuyerIdAndStatus(
            @PathVariable("buyerId") Long buyerId,
            @PathVariable("status") PaymentStatus status) {
        try {
            List<Payment> payments = paymentRepository.findByBuyerIdAndStatus(buyerId, status);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by invoice ID and status
    @GetMapping("/invoice/{invoiceId}/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByInvoiceIdAndStatus(
            @PathVariable("invoiceId") Long invoiceId,
            @PathVariable("status") PaymentStatus status) {
        try {
            List<Payment> payments = paymentRepository.findByInvoiceIdAndStatus(invoiceId, status);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Payment> payments = paymentRepository.findByCreatedAtBetween(startDate, endDate);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<List<Payment>> getPaymentsByAmountRange(
            @RequestParam("minAmount") BigDecimal minAmount,
            @RequestParam("maxAmount") BigDecimal maxAmount) {
        try {
            List<Payment> payments = paymentRepository.findByAmountBetween(minAmount, maxAmount);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get total amount by status
    @GetMapping("/total-amount/status/{status}")
    public ResponseEntity<BigDecimal> getTotalAmountByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            BigDecimal totalAmount = paymentRepository.getTotalAmountByStatus(status);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get total amount by date range
    @GetMapping("/total-amount/date-range")
    public ResponseEntity<BigDecimal> getTotalAmountByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            BigDecimal totalAmount = paymentRepository.getTotalAmountByDateRange(startDate, endDate);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment by ID
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable("id") Long id, @RequestBody Payment payment) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();

                // Update fields that exist in the model
                paymentToUpdate.setAmount(payment.getAmount());
                paymentToUpdate.setStatus(payment.getStatus());
                paymentToUpdate.setMethod(payment.getMethod());
                paymentToUpdate.setInvoice(payment.getInvoice());
                paymentToUpdate.setBuyer(payment.getBuyer());

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update payment by ID
    @PatchMapping("/{id}")
    public ResponseEntity<Payment> partialUpdatePayment(@PathVariable("id") Long id, @RequestBody Payment payment) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();

                // Update only non-null fields
                if (payment.getAmount() != null) {
                    paymentToUpdate.setAmount(payment.getAmount());
                }
                if (payment.getStatus() != null) {
                    paymentToUpdate.setStatus(payment.getStatus());
                }
                if (payment.getMethod() != null) {
                    paymentToUpdate.setMethod(payment.getMethod());
                }
                if (payment.getInvoice() != null) {
                    paymentToUpdate.setInvoice(payment.getInvoice());
                }
                if (payment.getBuyer() != null) {
                    paymentToUpdate.setBuyer(payment.getBuyer());
                }

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable("id") Long id, @RequestParam("status") PaymentStatus status) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();
                paymentToUpdate.setStatus(status);

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment amount
    @PatchMapping("/{id}/amount")
    public ResponseEntity<Payment> updatePaymentAmount(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();
                paymentToUpdate.setAmount(amount);

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment method
    @PatchMapping("/{id}/method")
    public ResponseEntity<Payment> updatePaymentMethod(@PathVariable("id") Long id, @RequestParam("method") PaymentMethod method) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();
                paymentToUpdate.setMethod(method);

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Confirm payment (mark as completed)
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Payment> confirmPayment(@PathVariable("id") Long id) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment paymentToUpdate = existingPayment.get();
                paymentToUpdate.setStatus(PaymentStatus.COMPLETED);

                Payment updatedPayment = paymentRepository.save(paymentToUpdate);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payment by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable("id") Long id) {
        try {
            Optional<Payment> payment = paymentRepository.findById(id);
            if (payment.isPresent()) {
                paymentRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete all payments
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllPayments() {
        try {
            paymentRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payments by status
    @DeleteMapping("/status/{status}")
    public ResponseEntity<HttpStatus> deletePaymentsByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<Payment> payments = paymentRepository.findByStatus(status);
            if (!payments.isEmpty()) {
                paymentRepository.deleteAll(payments);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payments by buyer ID
    @DeleteMapping("/buyer/{buyerId}")
    public ResponseEntity<HttpStatus> deletePaymentsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Payment> payments = paymentRepository.findByBuyerId(buyerId);
            if (!payments.isEmpty()) {
                paymentRepository.deleteAll(payments);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional utility endpoints
    // GET - Get payment count
    @GetMapping("/count")
    public ResponseEntity<Long> getPaymentCount() {
        try {
            long count = paymentRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getPaymentCountByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<Payment> payments = paymentRepository.findByStatus(status);
            return new ResponseEntity<>((long) payments.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payments made today
    @GetMapping("/today")
    public ResponseEntity<List<Payment>> getPaymentsToday() {
        try {
            LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
            List<Payment> payments = paymentRepository.findByCreatedAtBetween(startOfDay, endOfDay);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payments made this week
    @GetMapping("/this-week")
    public ResponseEntity<List<Payment>> getPaymentsThisWeek() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).toLocalDate().atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusDays(6).toLocalDate().atTime(23, 59, 59);
            List<Payment> payments = paymentRepository.findByCreatedAtBetween(startOfWeek, endOfWeek);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payments made this month
    @GetMapping("/this-month")
    public ResponseEntity<List<Payment>> getPaymentsThisMonth() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);
            List<Payment> payments = paymentRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total amount collected today
    @GetMapping("/total-amount/today")
    public ResponseEntity<BigDecimal> getTotalAmountToday() {
        try {
            LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
            BigDecimal totalAmount = paymentRepository.getTotalAmountByDateRange(startOfDay, endOfDay);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total amount collected this month
    @GetMapping("/total-amount/this-month")
    public ResponseEntity<BigDecimal> getTotalAmountThisMonth() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);
            BigDecimal totalAmount = paymentRepository.getTotalAmountByDateRange(startOfMonth, endOfMonth);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment statistics
    @GetMapping("/statistics")
    public ResponseEntity<PaymentStatisticsDTO> getPaymentStatistics() {
        try {
            PaymentStatisticsDTO stats = paymentService.getPaymentStatistics();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payments by invoice with summary
    @GetMapping("/invoice/{invoiceId}/summary")
    public ResponseEntity<PaymentSummaryDTO> getPaymentSummaryByInvoice(@PathVariable("invoiceId") Long invoiceId) {
        try {
            PaymentSummaryDTO summary = paymentService.getPaymentSummaryByInvoice(invoiceId);
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Process payment for invoice
    @PostMapping("/process-payment")
    public ResponseEntity<Payment> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            Payment payment = paymentService.processPayment(request);
            return new ResponseEntity<>(payment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PATCH - Bulk update payment status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<Payment>> bulkUpdatePaymentStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("status") PaymentStatus status) {
        try {
            List<Payment> updatedPayments = paymentService.bulkUpdateStatus(ids, status);
            return new ResponseEntity<>(updatedPayments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // GET - Get payment history for a buyer

    @GetMapping("/buyer/{buyerId}/history")
    public ResponseEntity<List<Payment>> getPaymentHistoryByBuyer(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Payment> payments = paymentRepository.findByBuyerId(buyerId);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// GET - Get revenue by project
    @GetMapping("/revenue/project/{projectId}")
    public ResponseEntity<BigDecimal> getRevenueByProject(@PathVariable("projectId") Long projectId) {
        try {
            BigDecimal revenue = paymentService.getTotalRevenueByProject(projectId);
            return new ResponseEntity<>(revenue, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// GET - Get overdue payments
    @GetMapping("/overdue")
    public ResponseEntity<List<Payment>> getOverduePayments() {
        try {
            List<Payment> payments = paymentService.getOverduePayments();
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// POST - Validate payment amount
    @PostMapping("/validate-amount")
    public ResponseEntity<Boolean> validatePaymentAmount(
            @RequestParam("invoiceId") Long invoiceId,
            @RequestParam("amount") BigDecimal amount) {
        try {
            boolean isValid = paymentService.validatePaymentAmount(invoiceId, amount);
            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// PATCH - Mark payment as failed
    @PatchMapping("/{id}/mark-failed")
    public ResponseEntity<Payment> markPaymentAsFailed(@PathVariable("id") Long id) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(id);
            if (existingPayment.isPresent()) {
                Payment payment = existingPayment.get();
                payment.setStatus(PaymentStatus.FAILED);
                Payment updatedPayment = paymentRepository.save(payment);
                return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unit/{unitId}/purchase")
    public ResponseEntity<PurchaseUnitResponse> purchaseUnit(
            @PathVariable Long unitId,
            @RequestBody PurchaseUnitRequest dto
    ) {
        PurchaseUnitResponse response = purchaseService.handlePurchase(unitId, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mpesa-stk/unit/{unitId}/purchase")
    public ResponseEntity<?> initiateStkPush(@Valid
            @RequestBody StkPushRequest request, @PathVariable("unitId") Long unitId) {
        try {
            // Validate phone number format (Kenyan format)
            if (!isValidKenyanPhoneNumber(request.getMpesaNumber())) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Invalid phone number format. Use format: 254XXXXXXXXX"
                        ));
            }

            // Validate amount
            if (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.ONE) < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                "success", false,
                                "message", "Amount must be greater than 0"
                        ));
            }
            Invoice invoice = invoiceService.createInvoiceForUnit(request.getUnitId(), request.getBuyerId());
            request.setInvoiceId(invoice.getId());
            StkPushResponse response = mpesaStkService.initiateStkPush(request.getMpesaNumber(), request.getTotalAmount(), request.getInvoiceId());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to initiate payment: " + e.getMessage()
                    ));
        }
    }

    // GET - Check STK Push status
    @GetMapping("/mpesa/status/{checkoutRequestId}")
    public ResponseEntity<?> checkStkPushStatus(@PathVariable("checkoutRequestId") String checkoutRequestId
    ) {
        try {
            Optional<MpesaPayment> payment = mpesaPaymentRepository.findByCheckoutRequestId(checkoutRequestId);

            if (payment.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            MpesaPayment mpesaPayment = payment.get();

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("checkoutRequestId", mpesaPayment.getCheckoutRequestId());
            responseMap.put("merchantRequestId", mpesaPayment.getMerchantRequestId());
            responseMap.put("status", mpesaPayment.getStatus());
            responseMap.put("amount", mpesaPayment.getAmount());
            responseMap.put("phoneNumber", mpesaPayment.getPhoneNumber());
            responseMap.put("mpesaReceiptNumber", mpesaPayment.getMpesaReceiptNumber());
            responseMap.put("resultCode", mpesaPayment.getResultCode());
            responseMap.put("resultDesc", mpesaPayment.getResultDesc());
            responseMap.put("isSuccessful", mpesaPayment.isSuccessful());
            responseMap.put("isPending", mpesaPayment.isPending());
            responseMap.put("isFailed", mpesaPayment.isFailed());

            return ResponseEntity.ok(responseMap);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to check payment status: " + e.getMessage()
                    ));
        }
    }

    // GET - Get all M-Pesa payments for an invoice
    @GetMapping("/mpesa/invoice/{invoiceId}")
    public ResponseEntity<List<MpesaPayment>> getMpesaPaymentsByInvoice(@PathVariable("invoiceId") Long invoiceId
    ) {
        try {
            List<MpesaPayment> payments = mpesaPaymentRepository.findByInvoiceIdOrderByCreatedAtDesc(invoiceId);

            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get M-Pesa payment history for a buyer
    @GetMapping("/mpesa/buyer/{buyerId}")
    public ResponseEntity<List<MpesaPayment>> getMpesaPaymentsByBuyer(@PathVariable("buyerId") Long buyerId
    ) {
        try {
            List<MpesaPayment> payments = mpesaPaymentRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId);

            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Add this private helper method for phone number validation

    private boolean isValidKenyanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }

        // Remove any spaces or special characters
        String cleanNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

        // Check if it matches Kenyan phone number patterns
        // Format: 254XXXXXXXXX (where X is 0-9 and total length is 12)
        return cleanNumber.matches("^254[17]\\d{8}$");
    }

    // Fixed M-Pesa callback handler
    @PostMapping("/mpesa/callback")
    public ResponseEntity<?> handleCallback(@RequestBody MpesaCallbackRequest request) {
        try {
            // Extract callback data from the request body
            Map<String, Object> body = request.getBody();
            if (body == null) {
                return ResponseEntity.badRequest().body(Map.of("ResultCode", 1, "ResultDesc", "Invalid request body"));
            }

            Map<String, Object> stkCallback = (Map<String, Object>) body.get("stkCallback");
            if (stkCallback == null) {
                return ResponseEntity.badRequest().body(Map.of("ResultCode", 1, "ResultDesc", "Invalid callback structure"));
            }

            String checkoutRequestId = (String) stkCallback.get("CheckoutRequestID");
            Integer resultCode = (Integer) stkCallback.get("ResultCode");

            if (checkoutRequestId == null) {
                return ResponseEntity.badRequest().body(Map.of("ResultCode", 1, "ResultDesc", "Checkout ID cannot be null"));
            }

            // Find the M-Pesa payment record
            Optional<MpesaPayment> optionalPayment = mpesaPaymentRepository.findByCheckoutRequestId(checkoutRequestId);

            if (optionalPayment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("ResultCode", 1, "ResultDesc", "Payment not found"));
            }

            MpesaPayment payment = optionalPayment.get();

            // Update payment with callback data
            payment.setResultCode(resultCode != null ? resultCode.toString() : null);
            payment.setResultDesc((String) stkCallback.get("ResultDesc"));
            payment.setCallbackReceivedAt(LocalDateTime.now());
            payment.setStatus(resultCode != null && resultCode == 0 ? "SUCCESS" : "FAILED");

            // If payment was successful, extract additional metadata
            if (resultCode != null && resultCode == 0) {
                Map<String, Object> callbackMetadata = (Map<String, Object>) stkCallback.get("CallbackMetadata");
                if (callbackMetadata != null) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) callbackMetadata.get("Item");
                    if (items != null) {
                        for (Map<String, Object> item : items) {
                            String name = (String) item.get("Name");
                            if ("MpesaReceiptNumber".equals(name)) {
                                payment.setMpesaReceiptNumber((String) item.get("Value"));
                            } else if ("TransactionDate".equals(name)) {
                                payment.setTransactionDate(String.valueOf(item.get("Value")));
                            }
                        }
                    }
                }

                // Update invoice status if fully paid
                Invoice invoice = payment.getInvoice();
                if (invoice != null && invoice.getTotalMpesaPayments().compareTo(invoice.getTotalAmount()) >= 0) {
                    invoice.setStatus(InvoiceStatus.PAID);
                    invoiceRepository.save(invoice);
                }
            }

            // Save the updated payment
            mpesaPaymentRepository.save(payment);

            return ResponseEntity.ok(Map.of("ResultCode", 0, "ResultDesc", "Success"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("ResultCode", 1, "ResultDesc", "Processing error: " + e.getMessage()));
        }
    }

}
