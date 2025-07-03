package com.amarsalimprojects.real_estate_app.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.amarsalimprojects.real_estate_app.model.PaymentPlan;
import com.amarsalimprojects.real_estate_app.service.PaymentPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment-plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentPlanController {

    private final PaymentPlanService paymentPlanService;

    @GetMapping
    public ResponseEntity<List<PaymentPlan>> getAllPaymentPlans() {
        List<PaymentPlan> paymentPlans = paymentPlanService.getAllPaymentPlans();
        return ResponseEntity.ok(paymentPlans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentPlan> getPaymentPlanById(@PathVariable Long id) {
        PaymentPlan paymentPlan = paymentPlanService.getPaymentPlanById(id);
        return ResponseEntity.ok(paymentPlan);
    }

    @PostMapping
    public ResponseEntity<PaymentPlan> createPaymentPlan(@Valid @RequestBody PaymentPlan paymentPlan) {
        PaymentPlan createdPaymentPlan = paymentPlanService.createPaymentPlan(paymentPlan);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPaymentPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentPlan> updatePaymentPlan(
            @PathVariable Long id,
            @Valid @RequestBody PaymentPlan paymentPlan) {
        PaymentPlan updatedPaymentPlan = paymentPlanService.updatePaymentPlan(id, paymentPlan);
        return ResponseEntity.ok(updatedPaymentPlan);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentPlan> partiallyUpdatePaymentPlan(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        PaymentPlan updatedPaymentPlan = paymentPlanService.partiallyUpdatePaymentPlan(id, updates);
        return ResponseEntity.ok(updatedPaymentPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentPlan(@PathVariable Long id) {
        paymentPlanService.deletePaymentPlan(id);
        return ResponseEntity.noContent().build();
    }
}
