package com.amarsalimprojects.real_estate_app.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amarsalimprojects.real_estate_app.model.PaymentPlan;
import com.amarsalimprojects.real_estate_app.repository.PaymentPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentPlanService {

    private final PaymentPlanRepository paymentPlanRepository;

    public List<PaymentPlan> getAllPaymentPlans() {
        return paymentPlanRepository.findAll();
    }

    public PaymentPlan getPaymentPlanById(Long id) {
        return paymentPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment plan not found with id: " + id));
    }

    public PaymentPlan createPaymentPlan(PaymentPlan paymentPlan) {
        return paymentPlanRepository.save(paymentPlan);
    }

    public PaymentPlan updatePaymentPlan(Long id, PaymentPlan paymentPlan) {
        PaymentPlan existingPaymentPlan = getPaymentPlanById(id);

        // Update all fields
        existingPaymentPlan.setName(paymentPlan.getName());
        existingPaymentPlan.setDescription(paymentPlan.getDescription());
        existingPaymentPlan.setPeriodMonths(paymentPlan.getPeriodMonths());
        existingPaymentPlan.setInterestRate(paymentPlan.getInterestRate());
        existingPaymentPlan.setMinDownPaymentPercentage(paymentPlan.getMinDownPaymentPercentage());
        existingPaymentPlan.setFlexible(paymentPlan.isFlexible());
        existingPaymentPlan.setActive(paymentPlan.isActive());
        existingPaymentPlan.setDisplayOrder(paymentPlan.getDisplayOrder());
        existingPaymentPlan.setPlanType(paymentPlan.getPlanType());
        existingPaymentPlan.setBenefits(paymentPlan.getBenefits());
        existingPaymentPlan.setTermsAndConditions(paymentPlan.getTermsAndConditions());
        existingPaymentPlan.setProcessingFeePercentage(paymentPlan.getProcessingFeePercentage());
        existingPaymentPlan.setEarlyPaymentDiscount(paymentPlan.getEarlyPaymentDiscount());

        return paymentPlanRepository.save(existingPaymentPlan);
    }

    public PaymentPlan partiallyUpdatePaymentPlan(Long id, Map<String, Object> updates) {
        PaymentPlan existingPaymentPlan = getPaymentPlanById(id);

        updates.forEach((key, value) -> {
            try {
                Field field = PaymentPlan.class.getDeclaredField(key);
                field.setAccessible(true);

                // Handle type conversion for specific fields
                Object convertedValue = convertValue(field.getType(), value);
                field.set(existingPaymentPlan, convertedValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + key, e);
            }
        });

        return paymentPlanRepository.save(existingPaymentPlan);
    }

    public void deletePaymentPlan(Long id) {
        PaymentPlan paymentPlan = getPaymentPlanById(id);
        paymentPlanRepository.delete(paymentPlan);
    }

    private Object convertValue(Class<?> fieldType, Object value) {
        if (value == null) {
            return null;
        }

        if (fieldType == BigDecimal.class && value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else if (fieldType == Integer.class && value instanceof Number) {
            return ((Number) value).intValue();
        } else if (fieldType == Boolean.class || fieldType == boolean.class) {
            return Boolean.valueOf(value.toString());
        }

        return value;
    }
}
