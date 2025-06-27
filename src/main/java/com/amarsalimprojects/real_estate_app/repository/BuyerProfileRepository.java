package com.amarsalimprojects.real_estate_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.model.BuyerProfile;

@Repository
public interface BuyerProfileRepository extends JpaRepository<BuyerProfile, Long> {

    Optional<BuyerProfile> findByEmail(String email);

    // List<BuyerProfile> findByCountyIgnoreCase(String county);
    Optional<BuyerProfile> findByNationalId(String nationalId);

    List<BuyerProfile> findByPhoneNumberContaining(String phone);
}
