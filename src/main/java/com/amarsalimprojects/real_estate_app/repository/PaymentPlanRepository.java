package com.amarsalimprojects.real_estate_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.model.PaymentPlan;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {

    List<PaymentPlan> findByIsActiveTrue();

    List<PaymentPlan> findByPlanType(String planType);

    @Query("SELECT p FROM PaymentPlan p WHERE p.isActive = true ORDER BY p.displayOrder ASC")
    List<PaymentPlan> findActivePaymentPlansOrderByDisplayOrder();
}
