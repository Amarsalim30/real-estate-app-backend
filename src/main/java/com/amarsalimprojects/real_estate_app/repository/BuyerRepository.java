package com.amarsalimprojects.real_estate_app.repository;

import com.amarsalimprojects.real_estate_app.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findByEmail(String email);

    List<Buyer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    List<Buyer> findByCityIgnoreCase(String city);

    List<Buyer> findByStateIgnoreCase(String state);
}
