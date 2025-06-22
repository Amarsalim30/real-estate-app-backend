package com.amarsalimprojects.real_estate_app.controller;

import com.amarsalimprojects.real_estate_app.model.PaymentDetail;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.repository.PaymentDetailRepository;
import com.amarsalimprojects.real_estate_app.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-details")
@CrossOrigin(origins = "*")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentDetailService paymentDetailService;

    // CREATE - Add a new payment detail
    @PostMapping
    public ResponseEntity<PaymentDetail> createPaymentDetail(@RequestBody PaymentDetail paymentDetail) {
        try {
            // Check if transaction ID already exists (if provided)
            if (paymentDetail.getTransactionId() != null && !paymentDetail.getTransactionId().isEmpty()) {
                Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findByTransactionId(paymentDetail.getTransactionId());
                if (existingPaymentDetail.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            // Set default status if not provided
            if (paymentDetail.getStatus() == null) {
                paymentDetail.setStatus(PaymentStatus.PENDING);
            }

            PaymentDetail savedPaymentDetail = paymentDetailRepository.save(paymentDetail);
            return new ResponseEntity<>(savedPaymentDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all payment details
    @GetMapping
    public ResponseEntity<List<PaymentDetail>> getAllPaymentDetails() {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findAll();
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment detail by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetail> getPaymentDetailById(@PathVariable("id") Long id) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findById(id);
            if (paymentDetail.isPresent()) {
                return new ResponseEntity<>(paymentDetail.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment detail by transaction ID
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<PaymentDetail> getPaymentDetailByTransactionId(@PathVariable("transactionId") String transactionId) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findByTransactionId(transactionId);
            if (paymentDetail.isPresent()) {
                return new ResponseEntity<>(paymentDetail.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatus(status);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by payment method
    @GetMapping("/method/{method}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByMethod(@PathVariable("method") PaymentMethod method) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByMethod(method);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByBuyerId(buyerId);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by invoice ID
    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByInvoiceId(@PathVariable("invoiceId") Long invoiceId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByInvoiceId(invoiceId);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by payment ID
    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByPaymentId(@PathVariable("paymentId") Long paymentId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentId(paymentId);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by buyer ID and status
    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByBuyerIdAndStatus(
            @PathVariable("buyerId") Long buyerId,
            @PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByBuyerIdAndStatus(buyerId, status);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by invoice ID and status
    @GetMapping("/invoice/{invoiceId}/status/{status}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByInvoiceIdAndStatus(
            @PathVariable("invoiceId") Long invoiceId,
            @PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByInvoiceIdAndStatus(invoiceId, status);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by payment method and status
    @GetMapping("/method/{method}/status/{status}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByMethodAndStatus(
            @PathVariable("method") PaymentMethod method,
            @PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByMethodAndStatus(method, status);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCreatedAtBetween(startDate, endDate);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByAmountRange(
            @RequestParam("minAmount") BigDecimal minAmount,
            @RequestParam("maxAmount") BigDecimal maxAmount) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByAmountBetween(minAmount, maxAmount);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by status and date range
    @GetMapping("/status/{status}/date-range")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByStatusAndDateRange(
            @PathVariable("status") PaymentStatus status,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatusAndCreatedAtBetween(status, startDate, endDate);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by bank name (from embedded BankDetails)
    @GetMapping("/bank/{bankName}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByBankName(@PathVariable("bankName") String bankName) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByBankDetailsBankName(bankName);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by card brand (from embedded CardDetails)
    @GetMapping("/card-brand/{brand}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByCardBrand(@PathVariable("brand") String brand) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCardDetailsBrand(brand);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment detail by check number (from embedded CheckDetails)
    @GetMapping("/check/{checkNumber}")
    public ResponseEntity<PaymentDetail> getPaymentDetailByCheckNumber(@PathVariable("checkNumber") String checkNumber) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findByCheckDetailsCheckNumber(checkNumber);
            if (paymentDetail.isPresent()) {
                return new ResponseEntity<>(paymentDetail.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get total amount by status
    @GetMapping("/total-amount/status/{status}")
    public ResponseEntity<BigDecimal> getTotalAmountByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            BigDecimal totalAmount = paymentDetailRepository.getTotalAmountByStatus(status);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // READ - Get count by payment method and status

    @GetMapping("/count/method/{method}/status/{status}")
    public ResponseEntity<Long> getCountByMethodAndStatus(
            @PathVariable("method") PaymentMethod method,
            @PathVariable("status") PaymentStatus status) {
        try {
            Long count = paymentDetailRepository.countByMethodAndStatus(method, status);
            return new ResponseEntity<>(count != null ? count : 0L, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment detail by ID
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDetail> updatePaymentDetail(@PathVariable("id") Long id, @RequestBody PaymentDetail paymentDetail) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();

                // Update fields that exist in the model
                paymentDetailToUpdate.setAmount(paymentDetail.getAmount());
                paymentDetailToUpdate.setStatus(paymentDetail.getStatus());
                paymentDetailToUpdate.setMethod(paymentDetail.getMethod());
                paymentDetailToUpdate.setTransactionId(paymentDetail.getTransactionId());
                paymentDetailToUpdate.setBuyer(paymentDetail.getBuyer());
                paymentDetailToUpdate.setInvoice(paymentDetail.getInvoice());
                paymentDetailToUpdate.setPayment(paymentDetail.getPayment());
                paymentDetailToUpdate.setBankDetails(paymentDetail.getBankDetails());
                paymentDetailToUpdate.setCardDetails(paymentDetail.getCardDetails());
                paymentDetailToUpdate.setCheckDetails(paymentDetail.getCheckDetails());

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update payment detail by ID
    @PatchMapping("/{id}")
    public ResponseEntity<PaymentDetail> partialUpdatePaymentDetail(@PathVariable("id") Long id, @RequestBody PaymentDetail paymentDetail) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();

                // Update only non-null fields
                if (paymentDetail.getAmount() != null) {
                    paymentDetailToUpdate.setAmount(paymentDetail.getAmount());
                }
                if (paymentDetail.getStatus() != null) {
                    paymentDetailToUpdate.setStatus(paymentDetail.getStatus());
                }
                if (paymentDetail.getMethod() != null) {
                    paymentDetailToUpdate.setMethod(paymentDetail.getMethod());
                }
                if (paymentDetail.getTransactionId() != null) {
                    paymentDetailToUpdate.setTransactionId(paymentDetail.getTransactionId());
                }
                if (paymentDetail.getBuyer() != null) {
                    paymentDetailToUpdate.setBuyer(paymentDetail.getBuyer());
                }
                if (paymentDetail.getInvoice() != null) {
                    paymentDetailToUpdate.setInvoice(paymentDetail.getInvoice());
                }
                if (paymentDetail.getPayment() != null) {
                    paymentDetailToUpdate.setPayment(paymentDetail.getPayment());
                }
                if (paymentDetail.getBankDetails() != null) {
                    paymentDetailToUpdate.setBankDetails(paymentDetail.getBankDetails());
                }
                if (paymentDetail.getCardDetails() != null) {
                    paymentDetailToUpdate.setCardDetails(paymentDetail.getCardDetails());
                }
                if (paymentDetail.getCheckDetails() != null) {
                    paymentDetailToUpdate.setCheckDetails(paymentDetail.getCheckDetails());
                }

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment detail status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDetail> updatePaymentDetailStatus(@PathVariable("id") Long id, @RequestParam("status") PaymentStatus status) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setStatus(status);

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment detail amount
    @PatchMapping("/{id}/amount")
    public ResponseEntity<PaymentDetail> updatePaymentDetailAmount(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setAmount(amount);

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update payment detail method
    @PatchMapping("/{id}/method")
    public ResponseEntity<PaymentDetail> updatePaymentDetailMethod(@PathVariable("id") Long id, @RequestParam("method") PaymentMethod method) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setMethod(method);

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update transaction ID
    @PatchMapping("/{id}/transaction-id")
    public ResponseEntity<PaymentDetail> updateTransactionId(@PathVariable("id") Long id, @RequestParam("transactionId") String transactionId) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setTransactionId(transactionId);

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Confirm payment detail (mark as completed)
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<PaymentDetail> confirmPaymentDetail(@PathVariable("id") Long id) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setStatus(PaymentStatus.COMPLETED);

                PaymentDetail updatedPaymentDetail = paymentDetailRepository.save(paymentDetailToUpdate);
                return new ResponseEntity<>(updatedPaymentDetail, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payment detail by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePaymentDetail(@PathVariable("id") Long id) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findById(id);
            if (paymentDetail.isPresent()) {
                paymentDetailRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete all payment details
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllPaymentDetails() {
        try {
            paymentDetailRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payment details by status
    @DeleteMapping("/status/{status}")
    public ResponseEntity<HttpStatus> deletePaymentDetailsByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatus(status);
            if (!paymentDetails.isEmpty()) {
                paymentDetailRepository.deleteAll(paymentDetails);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payment details by buyer ID
    @DeleteMapping("/buyer/{buyerId}")
    public ResponseEntity<HttpStatus> deletePaymentDetailsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByBuyerId(buyerId);
            if (!paymentDetails.isEmpty()) {
                paymentDetailRepository.deleteAll(paymentDetails);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete payment details by payment ID
    @DeleteMapping("/payment/{paymentId}")
    public ResponseEntity<HttpStatus> deletePaymentDetailsByPaymentId(@PathVariable("paymentId") Long paymentId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentId(paymentId);
            if (!paymentDetails.isEmpty()) {
                paymentDetailRepository.deleteAll(paymentDetails);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional utility endpoints
    // GET - Get payment detail count
    @GetMapping("/count")
    public ResponseEntity<Long> getPaymentDetailCount() {
        try {
            long count = paymentDetailRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment detail count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getPaymentDetailCountByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatus(status);
            return new ResponseEntity<>((long) paymentDetails.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details made today
    @GetMapping("/today")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsToday() {
        try {
            LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCreatedAtBetween(startOfDay, endOfDay);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details made this week
    @GetMapping("/this-week")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsThisWeek() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).toLocalDate().atStartOfDay();
            LocalDateTime endOfWeek = startOfWeek.plusDays(6).toLocalDate().atTime(23, 59, 59);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCreatedAtBetween(startOfWeek, endOfWeek);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details made this month
    @GetMapping("/this-month")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsThisMonth() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).toLocalDate().atTime(23, 59, 59);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
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
            BigDecimal totalAmount = paymentDetailRepository.getTotalAmountByDateRange(startOfDay, endOfDay);
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
            BigDecimal totalAmount = paymentDetailRepository.getTotalAmountByDateRange(startOfMonth, endOfMonth);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment detail statistics
    @GetMapping("/statistics")
    public ResponseEntity<PaymentDetailStatisticsDTO> getPaymentDetailStatistics() {
        try {
            PaymentDetailStatisticsDTO stats = paymentDetailService.getPaymentDetailStatistics();
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details by payment method breakdown
    @GetMapping("/breakdown/method")
    public ResponseEntity<List<PaymentMethodBreakdownDTO>> getPaymentMethodBreakdown() {
        try {
            List<PaymentMethodBreakdownDTO> breakdown = paymentDetailService.getPaymentMethodBreakdown();
            return new ResponseEntity<>(breakdown, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Create payment detail with bank details
    @PostMapping("/bank-payment")
    public ResponseEntity<PaymentDetail> createBankPaymentDetail(@RequestBody CreateBankPaymentRequest request) {
        try {
            PaymentDetail paymentDetail = paymentDetailService.createBankPaymentDetail(request);
            return new ResponseEntity<>(paymentDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Create payment detail with card details
    @PostMapping("/card-payment")
    public ResponseEntity<PaymentDetail> createCardPaymentDetail(@RequestBody CreateCardPaymentRequest request) {
        try {
            PaymentDetail paymentDetail = paymentDetailService.createCardPaymentDetail(request);
            return new ResponseEntity<>(paymentDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Create payment detail with check details
    @PostMapping("/check-payment")
    public ResponseEntity<PaymentDetail> createCheckPaymentDetail(@RequestBody CreateCheckPaymentRequest request) {
        try {
            PaymentDetail paymentDetail = paymentDetailService.createCheckPaymentDetail(request);
            return new ResponseEntity<>(paymentDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PATCH - Bulk update payment detail status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<PaymentDetail>> bulkUpdatePaymentDetailStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("status") PaymentStatus status) {
        try {
            List<PaymentDetail> updatedPaymentDetails = paymentDetailService.bulkUpdateStatus(ids, status);
            return new ResponseEntity<>(updatedPaymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get failed payment details for retry
    @GetMapping("/failed-for-retry")
    public ResponseEntity<List<PaymentDetail>> getFailedPaymentDetailsForRetry() {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatus(PaymentStatus.FAILED);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST - Retry failed payment detail
    @PostMapping("/{id}/retry")
    public ResponseEntity<PaymentDetail> retryPaymentDetail(@PathVariable("id") Long id) {
        try {
            PaymentDetail retryResult = paymentDetailService.retryPaymentDetail(id);
            return new ResponseEntity<>(retryResult, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details audit trail
    @GetMapping("/{id}/audit-trail")
    public ResponseEntity<List<PaymentDetailAuditDTO>> getPaymentDetailAuditTrail(@PathVariable("id") Long id) {
        try {
            List<PaymentDetailAuditDTO> auditTrail = paymentDetailService.getPaymentDetailAuditTrail(id);
            return new ResponseEntity<>(auditTrail, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
