package com.amarsalimprojects.real_estate_app.controller;

import com.amarsalimprojects.real_estate_app.model.PaymentDetail;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.model.enums.PaymentStatus;
import com.amarsalimprojects.real_estate_app.repository.PaymentDetailRepository;
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

    // CREATE - Add a new payment detail
    @PostMapping
    public ResponseEntity<PaymentDetail> createPaymentDetail(@RequestBody PaymentDetail paymentDetail) {
        try {
            // Check if payment number already exists (if provided)
            if (paymentDetail.getPaymentNumber() != null && !paymentDetail.getPaymentNumber().isEmpty()) {
                Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findByPaymentNumber(paymentDetail.getPaymentNumber());
                if (existingPaymentDetail.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            // Check if transaction ID already exists (if provided)
            if (paymentDetail.getTransactionId() != null && !paymentDetail.getTransactionId().isEmpty()) {
                Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findByTransactionId(paymentDetail.getTransactionId());
                if (existingPaymentDetail.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
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

    // READ - Get payment detail by payment number
    @GetMapping("/payment-number/{paymentNumber}")
    public ResponseEntity<PaymentDetail> getPaymentDetailByPaymentNumber(@PathVariable("paymentNumber") String paymentNumber) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findByPaymentNumber(paymentNumber);
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
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentMethod(method);
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
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentMethodAndStatus(method, status);
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
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startDate, endDate);
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
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByStatusAndPaymentDateBetween(status, startDate, endDate);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by processing fee greater than
    @GetMapping("/processing-fee/{fee}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByProcessingFeeGreaterThan(@PathVariable("fee") BigDecimal fee) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByProcessingFeeGreaterThan(fee);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by bank name
    @GetMapping("/bank/{bankName}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByBankName(@PathVariable("bankName") String bankName) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByBankName(bankName);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment details by card brand
    @GetMapping("/card-brand/{brand}")
    public ResponseEntity<List<PaymentDetail>> getPaymentDetailsByCardBrand(@PathVariable("brand") String brand) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByCardBrand(brand);
            if (paymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get payment detail by check number
    @GetMapping("/check/{checkNumber}")
    public ResponseEntity<PaymentDetail> getPaymentDetailByCheckNumber(@PathVariable("checkNumber") String checkNumber) {
        try {
            Optional<PaymentDetail> paymentDetail = paymentDetailRepository.findByCheckNumber(checkNumber);
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

    // READ - Get total processing fee by status
    @GetMapping("/total-processing-fee/status/{status}")
    public ResponseEntity<BigDecimal> getTotalProcessingFeeByStatus(@PathVariable("status") PaymentStatus status) {
        try {
            BigDecimal totalProcessingFee = paymentDetailRepository.getTotalProcessingFeeByStatus(status);
            return new ResponseEntity<>(totalProcessingFee != null ? totalProcessingFee : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get count by payment method and status
    @GetMapping("/count/method/{method}/status/{status}")
    public ResponseEntity<Long> getCountByPaymentMethodAndStatus(
            @PathVariable("method") PaymentMethod method,
            @PathVariable("status") PaymentStatus status) {
        try {
            Long count = paymentDetailRepository.countByPaymentMethodAndStatus(method, status);
            return new ResponseEntity<>(count, HttpStatus.OK);
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

                // Update fields
                paymentDetailToUpdate.setPaymentNumber(paymentDetail.getPaymentNumber());
                paymentDetailToUpdate.setBuyer(paymentDetail.getBuyer());
                paymentDetailToUpdate.setInvoice(paymentDetail.getInvoice());
                paymentDetailToUpdate.setAmount(paymentDetail.getAmount());
                paymentDetailToUpdate.setProcessingFee(paymentDetail.getProcessingFee());
                paymentDetailToUpdate.setPaymentMethod(paymentDetail.getPaymentMethod());
                paymentDetailToUpdate.setPaymentDate(paymentDetail.getPaymentDate());
                paymentDetailToUpdate.setStatus(paymentDetail.getStatus());
                paymentDetailToUpdate.setTransactionId(paymentDetail.getTransactionId());
                paymentDetailToUpdate.setDescription(paymentDetail.getDescription());
                paymentDetailToUpdate.setNotes(paymentDetail.getNotes());
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
                if (paymentDetail.getPaymentNumber() != null) {
                    paymentDetailToUpdate.setPaymentNumber(paymentDetail.getPaymentNumber());
                }
                if (paymentDetail.getBuyer() != null) {
                    paymentDetailToUpdate.setBuyer(paymentDetail.getBuyer());
                }
                if (paymentDetail.getInvoice() != null) {
                    paymentDetailToUpdate.setInvoice(paymentDetail.getInvoice());
                }
                if (paymentDetail.getAmount() != null) {
                    paymentDetailToUpdate.setAmount(paymentDetail.getAmount());
                }
                if (paymentDetail.getProcessingFee() != null) {
                    paymentDetailToUpdate.setProcessingFee(paymentDetail.getProcessingFee());
                }
                if (paymentDetail.getPaymentMethod() != null) {
                    paymentDetailToUpdate.setPaymentMethod(paymentDetail.getPaymentMethod());
                }
                if (paymentDetail.getPaymentDate() != null) {
                    paymentDetailToUpdate.setPaymentDate(paymentDetail.getPaymentDate());
                }
                if (paymentDetail.getStatus() != null) {
                    paymentDetailToUpdate.setStatus(paymentDetail.getStatus());
                }
                if (paymentDetail.getTransactionId() != null) {
                    paymentDetailToUpdate.setTransactionId(paymentDetail.getTransactionId());
                }
                if (paymentDetail.getDescription() != null) {
                    paymentDetailToUpdate.setDescription(paymentDetail.getDescription());
                }
                if (paymentDetail.getNotes() != null) {
                    paymentDetailToUpdate.setNotes(paymentDetail.getNotes());
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

    // UPDATE - Update processing fee
    @PatchMapping("/{id}/processing-fee")
    public ResponseEntity<PaymentDetail> updateProcessingFee(@PathVariable("id") Long id, @RequestParam("processingFee") BigDecimal processingFee) {
        try {
            Optional<PaymentDetail> existingPaymentDetail = paymentDetailRepository.findById(id);
            if (existingPaymentDetail.isPresent()) {
                PaymentDetail paymentDetailToUpdate = existingPaymentDetail.get();
                paymentDetailToUpdate.setProcessingFee(processingFee);

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

    // DELETE - Delete payment details by invoice ID
    @DeleteMapping("/invoice/{invoiceId}")
    public ResponseEntity<HttpStatus> deletePaymentDetailsByInvoiceId(@PathVariable("invoiceId") Long invoiceId) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByInvoiceId(invoiceId);
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

    // DELETE - Delete payment details by payment method
    @DeleteMapping("/method/{method}")
    public ResponseEntity<HttpStatus> deletePaymentDetailsByMethod(@PathVariable("method") PaymentMethod method) {
        try {
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentMethod(method);
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
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfDay, endOfDay);
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
            LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfWeek = startOfWeek.plusDays(6).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfWeek, endOfWeek);
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
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfMonth, endOfMonth);
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
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfDay, endOfDay);
            BigDecimal totalAmount = paymentDetails.stream()
                    .map(PaymentDetail::getAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total amount collected this month
    @GetMapping("/total-amount/this-month")
    public ResponseEntity<BigDecimal> getTotalAmountThisMonth() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfMonth, endOfMonth);
            BigDecimal totalAmount = paymentDetails.stream()
                    .map(PaymentDetail::getAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total processing fees collected today
    @GetMapping("/total-processing-fee/today")
    public ResponseEntity<BigDecimal> getTotalProcessingFeeToday() {
        try {
            LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfDay, endOfDay);
            BigDecimal totalProcessingFee = paymentDetails.stream()
                    .map(PaymentDetail::getProcessingFee)
                    .filter(fee -> fee != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ResponseEntity<>(totalProcessingFee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total processing fees collected this month
    @GetMapping("/total-processing-fee/this-month")
    public ResponseEntity<BigDecimal> getTotalProcessingFeeThisMonth() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            List<PaymentDetail> paymentDetails = paymentDetailRepository.findByPaymentDateBetween(startOfMonth, endOfMonth);
            BigDecimal totalProcessingFee = paymentDetails.stream()
                    .map(PaymentDetail::getProcessingFee)
                    .filter(fee -> fee != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ResponseEntity<>(totalProcessingFee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get payment details summary by payment method
    @GetMapping("/summary/payment-method")
    public ResponseEntity<List<Object[]>> getPaymentDetailsSummaryByMethod() {
        try {
            // This would typically be implemented with a custom query
            // For now, returning a simple response
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get recent payment details (last 10)
    @GetMapping("/recent")
    public ResponseEntity<List<PaymentDetail>> getRecentPaymentDetails() {
        try {
            List<PaymentDetail> allPaymentDetails = paymentDetailRepository.findAll();
            List<PaymentDetail> recentPaymentDetails = allPaymentDetails.stream()
                    .sorted((pd1, pd2) -> {
                        if (pd1.getPaymentDate() == null && pd2.getPaymentDate() == null) {
                            return 0;
                        }
                        if (pd1.getPaymentDate() == null) {
                            return 1;
                        }
                        if (pd2.getPaymentDate() == null) {
                            return -1;
                        }
                        return pd2.getPaymentDate().compareTo(pd1.getPaymentDate());
                    })
                    .limit(10)
                    .toList();

            if (recentPaymentDetails.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(recentPaymentDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
