package com.amarsalimprojects.real_estate_app.controller;

import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.enums.InvoiceStatus;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
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
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    // CREATE - Add a new invoice
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        try {
            // Check if invoice number already exists
            if (invoice.getInvoiceNumber() != null) {
                Optional<Invoice> existingInvoice = invoiceRepository.findByInvoiceNumber(invoice.getInvoiceNumber());
                if (existingInvoice.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
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

    // READ - Get invoice by invoice number
    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoiceByNumber(@PathVariable("invoiceNumber") String invoiceNumber) {
        try {
            Optional<Invoice> invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
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

    // READ - Get invoices by unit ID
    @GetMapping("/unit/{unitId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUnitId(@PathVariable("unitId") Long unitId) {
        try {
            List<Invoice> invoices = invoiceRepository.findByUnitId(unitId);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Invoice>> getInvoicesByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Invoice> invoices = invoiceRepository.findByProjectId(projectId);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by issue date range
    @GetMapping("/issue-date")
    public ResponseEntity<List<Invoice>> getInvoicesByIssueDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Invoice> invoices = invoiceRepository.findByIssueDateBetween(startDate, endDate);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get invoices by due date range
    @GetMapping("/due-date")
    public ResponseEntity<List<Invoice>> getInvoicesByDueDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Invoice> invoices = invoiceRepository.findByDueDateBetween(startDate, endDate);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get overdue invoices
    @GetMapping("/overdue")
    public ResponseEntity<List<Invoice>> getOverdueInvoices() {
        try {
            List<Invoice> invoices = invoiceRepository.findOverdueInvoices(LocalDate.now(), InvoiceStatus.PENDING);
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

                // Update fields
                invoiceToUpdate.setInvoiceNumber(invoice.getInvoiceNumber());
                invoiceToUpdate.setUnit(invoice.getUnit());
                invoiceToUpdate.setBuyer(invoice.getBuyer());
                invoiceToUpdate.setProject(invoice.getProject());
                invoiceToUpdate.setIssueDate(invoice.getIssueDate());
                invoiceToUpdate.setDueDate(invoice.getDueDate());
                invoiceToUpdate.setPaidDate(invoice.getPaidDate());
                invoiceToUpdate.setStatus(invoice.getStatus());
                invoiceToUpdate.setSubtotal(invoice.getSubtotal());
                invoiceToUpdate.setTaxAmount(invoice.getTaxAmount());
                invoiceToUpdate.setTotalAmount(invoice.getTotalAmount());
                invoiceToUpdate.setDescription(invoice.getDescription());
                invoiceToUpdate.setPaymentTerms(invoice.getPaymentTerms());
                invoiceToUpdate.setNotes(invoice.getNotes());

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
                if (invoice.getInvoiceNumber() != null) {
                    invoiceToUpdate.setInvoiceNumber(invoice.getInvoiceNumber());
                }
                if (invoice.getUnit() != null) {
                    invoiceToUpdate.setUnit(invoice.getUnit());
                }
                if (invoice.getBuyer() != null) {
                    invoiceToUpdate.setBuyer(invoice.getBuyer());
                }
                if (invoice.getProject() != null) {
                    invoiceToUpdate.setProject(invoice.getProject());
                }
                if (invoice.getIssueDate() != null) {
                    invoiceToUpdate.setIssueDate(invoice.getIssueDate());
                }
                if (invoice.getDueDate() != null) {
                    invoiceToUpdate.setDueDate(invoice.getDueDate());
                }
                if (invoice.getPaidDate() != null) {
                    invoiceToUpdate.setPaidDate(invoice.getPaidDate());
                }
                if (invoice.getStatus() != null) {
                    invoiceToUpdate.setStatus(invoice.getStatus());
                }
                if (invoice.getSubtotal() != null) {
                    invoiceToUpdate.setSubtotal(invoice.getSubtotal());
                }
                if (invoice.getTaxAmount() != null) {
                    invoiceToUpdate.setTaxAmount(invoice.getTaxAmount());
                }
                if (invoice.getTotalAmount() != null) {
                    invoiceToUpdate.setTotalAmount(invoice.getTotalAmount());
                }
                if (invoice.getDescription() != null) {
                    invoiceToUpdate.setDescription(invoice.getDescription());
                }
                if (invoice.getPaymentTerms() != null) {
                    invoiceToUpdate.setPaymentTerms(invoice.getPaymentTerms());
                }
                if (invoice.getNotes() != null) {
                    invoiceToUpdate.setNotes(invoice.getNotes());
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

                // Set paid date if status is PAID
                if (status == InvoiceStatus.PAID && invoiceToUpdate.getPaidDate() == null) {
                    invoiceToUpdate.setPaidDate(LocalDate.now());
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

    // UPDATE - Mark invoice as paid
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<Invoice> markInvoiceAsPaid(@PathVariable("id") Long id) {
        try {
            Optional<Invoice> existingInvoice = invoiceRepository.findById(id);
            if (existingInvoice.isPresent()) {
                Invoice invoiceToUpdate = existingInvoice.get();
                invoiceToUpdate.setStatus(InvoiceStatus.PAID);
                invoiceToUpdate.setPaidDate(LocalDate.now());

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

    // DELETE - Delete all invoices
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllInvoices() {
        try {
            invoiceRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    // Additional utility endpoints
    // GET - Get invoice count
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

    // GET - Get invoices due today
    @GetMapping("/due-today")
    public ResponseEntity<List<Invoice>> getInvoicesDueToday() {
        try {
            LocalDate today = LocalDate.now();
            List<Invoice> invoices = invoiceRepository.findByDueDateBetween(today, today);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get invoices due this week
    @GetMapping("/due-this-week")
    public ResponseEntity<List<Invoice>> getInvoicesDueThisWeek() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate endOfWeek = today.plusDays(7);
            List<Invoice> invoices = invoiceRepository.findByDueDateBetween(today, endOfWeek);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get invoices due this month
    @GetMapping("/due-this-month")
    public ResponseEntity<List<Invoice>> getInvoicesDueThisMonth() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfMonth = today.withDayOfMonth(1);
            LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
            List<Invoice> invoices = invoiceRepository.findByDueDateBetween(startOfMonth, endOfMonth);
            if (invoices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(invoices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
