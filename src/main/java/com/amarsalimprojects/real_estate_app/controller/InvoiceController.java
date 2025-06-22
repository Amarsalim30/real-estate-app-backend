package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    // CREATE - Add a new invoice
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        try {
            // Set issued date if not provided
            if (invoice.getIssuedDate() == null) {
                invoice.setIssuedDate(LocalDate.now());
            }

            // Set default status if not provided
            if (invoice.getStatus() == null) {
                invoice.setStatus(InvoiceStatus.PENDING);
            }

            Invoice savedInvoice = invoiceRepository.save(invoice);
            return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all invoices
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = invoiceRepository.findAll();
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoice by ID
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable("id") Long id) {
        try {
            Optional<Invoice> invoice = invoiceRepository.findById(id);
            if (invoice.isPresent()) {
                return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Invoice>> getInvoicesByStatus(@PathVariable("status") InvoiceStatus status) {
        try {
            List<Invoice> invoices = invoiceRepository.findByStatus(status);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Invoice>> getInvoicesByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Invoice> invoices = invoiceRepository.findByBuyerId(buyerId);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoice by unit ID
    @GetMapping("/unit/{unitId}")
    public ResponseEntity<Invoice> getInvoiceByUnitId(@PathVariable("unitId") Long unitId) {
        try {
            Optional<Invoice> invoice = invoiceRepository.findByUnitId(unitId);
            if (invoice.isPresent()) {
                return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by issued date range
    @GetMapping("/issued-date-range")
    public ResponseEntity<List<Invoice>> getInvoicesByIssuedDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Invoice> invoices = invoiceRepository.findByIssuedDateBetween(startDate, endDate);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by amount range
    @GetMapping("/amount-range")
    public ResponseEntity<List<Invoice>> getInvoicesByAmountRange(
            @RequestParam("minAmount") BigDecimal minAmount,
            @RequestParam("maxAmount") BigDecimal maxAmount) {
        try {
            List<Invoice> invoices = invoiceRepository.findByTotalAmountBetween(minAmount, maxAmount);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get total amount by status
    @GetMapping("/total-amount/{status}")
    public ResponseEntity<BigDecimal> getTotalAmountByStatus(@PathVariable("status") InvoiceStatus status) {
        try {
            BigDecimal totalAmount = invoiceRepository.getTotalAmountByStatus(status);
            return new ResponseEntity<>(totalAmount != null ? totalAmount : BigDecimal.ZERO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update invoice by ID
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
            if (existingInvoice.isPresent()) {
                Invoice invoiceToUpdate = existingInvoice.get();

                // Update fields that exist in the model
                invoiceToUpdate.setStatus(invoice.getStatus());
                invoiceToUpdate.setTotalAmount(invoice.getTotalAmount());
                invoiceToUpdate.setIssuedDate(invoice.getIssuedDate());
                invoiceToUpdate.setUnit(invoice.getUnit());
                invoiceToUpdate.setBuyer(invoice.getBuyer());

                Invoice updatedInvoice = invoiceRepository.save(invoiceToUpdate);
                return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update invoice by ID
    @PatchMapping("/{id}")
    public ResponseEntity<Invoice> partialUpdateInvoice(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
            if (existingInvoice.isPresent()) {
                Invoice invoiceToUpdate = existingInvoice.get();

                // Update only non-null fields
                if (invoice.getStatus() != null) {
                    invoiceToUpdate.setStatus(invoice.getStatus());
                }
                if (invoice.getTotalAmount() != null) {
                    invoiceToUpdate.setTotalAmount(invoice.getTotalAmount());
                }
                if (invoice.getIssuedDate() != null) {
                    invoiceToUpdate.setIssuedDate(invoice.getIssuedDate());
                }
                if (invoice.getUnit() != null) {
                    invoiceToUpdate.setUnit(invoice.getUnit());
                }
                if (invoice.getBuyer() != null) {
                    invoiceToUpdate.setBuyer(invoice.getBuyer());
                }

                Invoice updatedInvoice = invoiceRepository.save(invoiceToUpdate);
                return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update invoice status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Invoice> updateInvoiceStatus(@PathVariable("id") Long id, @RequestParam("status") InvoiceStatus status) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
            if (existingInvoice.isPresent()) {
                Invoice invoiceToUpdate = existingInvoice.get();
                invoiceToUpdate.setStatus(status);

                Invoice updatedInvoice = invoiceRepository.save(invoiceToUpdate);
                return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Mark invoice as paid
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<Invoice> markInvoiceAsPaid(@PathVariable("id") Long id) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
            if (existingInvoice.isPresent()) {
                Invoice invoiceToUpdate = existingInvoice.get();
                invoiceToUpdate.setStatus(InvoiceStatus.PAID);

                Invoice updatedInvoice = invoiceRepository.save(invoiceToUpdate);
                return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete invoice by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteInvoice(@PathVariable("id") Long id) {
        try {
            Optional<Invoice> invoice = invoiceRepository.findById(id);
            if (invoice.isPresent()) {
                invoiceRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete invoices by status
    @DeleteMapping("/status/{status}")
    public ResponseEntity<HttpStatus> deleteInvoicesByStatus(@PathVariable("status") InvoiceStatus status) {
        try {
            List<Invoice> invoices = invoiceRepository.findByStatus(status);
            if (!invoices.isEmpty()) {
                invoiceRepository.deleteAll(invoices);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Utility endpoint - Get invoice count
    @GetMapping("/count")
    public ResponseEntity<Long> getInvoiceCount() {
        try {
            long count = invoiceRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get invoice count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getInvoiceCountByStatus(@PathVariable("status") InvoiceStatus status) {
        try {
            List<Invoice> invoices = invoiceRepository.findByStatus(status);
            return new ResponseEntity<>((long) invoices.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    // GET - Get invoice with payment details
@GetMapping("/{id}/with-payments")
public ResponseEntity<InvoiceWithPaymentsDTO> getInvoiceWithPayments(@PathVariable Long id) {
    Optional<Invoice> invoice = invoiceRepository.findById(id);
    if (invoice.isPresent()) {
        InvoiceWithPaymentsDTO dto = mapToInvoiceWithPaymentsDTO(invoice.get());
        return ResponseEntity.ok(dto);
    }
    return ResponseEntity.notFound().build();
}

// GET - Get invoices summary/dashboard
@GetMapping("/summary")
public ResponseEntity<InvoiceSummaryDTO> getInvoicesSummary() {
    InvoiceSummaryDTO summary = InvoiceSummaryDTO.builder()
        .totalInvoices(invoiceRepository.count())
        .pendingCount(invoiceRepository.countByStatus(InvoiceStatus.PENDING))
        .paidCount(invoiceRepository.countByStatus(InvoiceStatus.PAID))
        .overdueCount(invoiceRepository.countByStatus(InvoiceStatus.OVERDUE))
        .totalAmount(invoiceRepository.getTotalAmountByStatus(null)) // All statuses
        .pendingAmount(invoiceRepository.getTotalAmountByStatus(InvoiceStatus.PENDING))
        .paidAmount(invoiceRepository.getTotalAmountByStatus(InvoiceStatus.PAID))
        .build();
    
    return ResponseEntity.ok(summary);
}

// GET - Get recent invoices
@GetMapping("/recent")
public ResponseEntity<List<Invoice>> getRecentInvoices(@RequestParam(defaultValue = "30") int days) {
    LocalDate cutoffDate = LocalDate.now().minusDays(days);
    List<Invoice> invoices = invoiceRepository.findByIssuedDateAfter(cutoffDate);
    return ResponseEntity.ok(invoices);
}

// GET - Get high-value invoices
@GetMapping("/high-value")
public ResponseEntity<List<Invoice>> getHighValueInvoices(@RequestParam BigDecimal threshold) {
    List<Invoice> invoices = invoiceRepository.findByTotalAmountGreaterThan(threshold);
    return ResponseEntity.ok(invoices);
}

// PATCH - Process payment for invoice
@PatchMapping("/{id}/process-payment")
public ResponseEntity<Invoice> processPayment(
    @PathVariable Long id, 
    @RequestParam BigDecimal amount) {
    try {
        invoiceService.processPayment(id, amount);
        Invoice updatedInvoice = invoiceRepository.findById(id).get();
        return ResponseEntity.ok(updatedInvoice);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

     */
}
