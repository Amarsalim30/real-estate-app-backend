package com.amarsalimprojects.real_estate_app.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.amarsalimprojects.real_estate_app.model.MpesaPayment;
import com.amarsalimprojects.real_estate_app.repository.MpesaPaymentRepository;

@Service
public class MpesaPaymentService {
    private final MpesaPaymentRepository repository;

    public MpesaPaymentService(MpesaPaymentRepository repository) {
        this.repository = repository;
    }

    public List<MpesaPayment> getAll() {
        return repository.findAll();
    }

    public Optional<MpesaPayment> getById(Long id) {
        return repository.findById(id);
    }

    public MpesaPayment create(MpesaPayment entity) {
        return repository.save(entity);
    }

    public MpesaPayment update(Long id, MpesaPayment entity) {
        if (repository.existsById(id)) {
            entity.setId(id);
            return repository.save(entity);
        } else {
            throw new RuntimeException("MpesaPayment not found");
        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
