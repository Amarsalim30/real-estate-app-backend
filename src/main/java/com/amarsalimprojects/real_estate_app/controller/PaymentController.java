package com.amarsalimprojects.real_estate_app.controller;

import com.amarsalimprojects.real_estate_app.model.Payment;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    // CREATE - Add a new payment
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        try {
            // Check if transaction ID already exists (if provided)
            if (payment.getTransactionId() != null && !payment.getTransactionId().isEmpty()) {
                Optional<Payment> existingPayment = paymentRepository.findByTransactionId(payment.getTransactionId());
                if (existingPayment.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
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

    // READ - Get payment by transaction ID
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<Payment> getPaymentByTransactionId(@PathVariable("transactionId") String transactionId) {
        try {
            Optional<Payment> payment = paymentRepository.findByTransactionId(transactionId);
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
            List<Payment> payments = paymentRepository.findByPaymentMethod(method);
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

    // READ - Get payments by unit ID
    @GetMapping("/unit/{unitId}")
    public ResponseEntity<List<Payment>> getPaymentsByUnitId(@PathVariable("unitId") Long unitId) {
        try {
            List<Payment> payments = paymentRepository.findByUnitId(unitId);
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
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
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

    // READ - Get payments by status and date range
    @GetMapping("/status/{status}/date-range")
    public ResponseEntity<List<Payment>> getPaymentsByStatusAndDateRange(
            @PathVariable("status") PaymentStatus status,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Payment> payments = paymentRepository.findByStatusAndPaymentDateBetween(status, startDate, endDate);
            if (payments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(payments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payments by specific date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Payment>> getPaymentsByDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Payment> payments = paymentRepository.findPaymentsByDate(date);
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
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
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

                // Update fields
                paymentToUpdate.setInvoice(payment.getInvoice());
                paymentToUpdate.setUnit(payment.getUnit());
                paymentToUpdate.setBuyer(payment.getBuyer());
                paymentToUpdate.setAmount(payment.getAmount());
                paymentToUpdate.setPaymentDate(payment.getPaymentDate());
                paymentToUpdate.setPaymentMethod(payment.getPaymentMethod());
                paymentToUpdate.setTransactionId(payment.getTransactionId());
                paymentToUpdate.setStatus(payment.getStatus());

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
                if (payment.getInvoice() != null) {
                    paymentToUpdate.setInvoice(payment.getInvoice());
                }
                if (payment.getUnit() != null) {
                    paymentToUpdate.setUnit(payment.getUnit());
                }
                if (payment.getBuyer() != null) {
                    paymentToUpdate.setBuyer(payment.getBuyer());
                }
                if (payment.getAmount() != null) {
                    paymentToUpdate.setAmount(payment.getAmount());
                }
                if (payment.getPaymentDate() != null) {
                    paymentToUpdate.setPaymentDate(payment.getPaymentDate());
                }
                if (payment.getPaymentMethod() != null) {
                    paymentToUpdate.setPaymentMethod(payment.getPaymentMethod());
                }
                if (payment.getTransactionId() != null) {
                    paymentToUpdate.setTransactionId(payment.getTransactionId());
                }
                if (payment.getStatus() != null) {
                    paymentToUpdate.setStatus(payment.getStatus());
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
                paymentToUpdate.setPaymentMethod(method);

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
            LocalDate today = LocalDate.now();
            List<Payment> payments = paymentRepository.findPaymentsByDate(today);
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
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);
            List<Payment> payments = paymentRepository.findByPaymentDateBetween(startOfWeek, endOfWeek);
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
            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
            List<Payment> payments = paymentRepository.findByPaymentDateBetween(startOfMonth, endOfMonth);
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
            LocalDate today = LocalDate.now();
            BigDecimal totalAmount = paymentRepository.getTotalAmountByDateRange(today, today);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total amount collected this month
    @GetMapping("/total-amount/this-month")
    public ResponseEntity<BigDecimal> getTotalAmountThisMonth() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
            BigDecimal totalAmount = paymentRepository.getTotalAmountByDateRange(startOfMonth, endOfMonth);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
