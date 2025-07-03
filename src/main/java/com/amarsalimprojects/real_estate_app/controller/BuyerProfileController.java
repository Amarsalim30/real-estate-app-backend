package com.amarsalimprojects.real_estate_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.amarsalimprojects.real_estate_app.model.BuyerProfile;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;

@RestController
@RequestMapping("/api/buyers")
@CrossOrigin(origins = "*")
public class BuyerProfileController {

    @Autowired
    private BuyerProfileRepository buyerProfileRepository;

    // CREATE - Add a new buyer
    @PostMapping
    public ResponseEntity<BuyerProfile> createBuyer(@RequestBody BuyerProfile buyer) {
        try {
            // Check if email already exists
            Optional<BuyerProfile> existingBuyer = buyerProfileRepository.findByEmail(buyer.getEmail());
            if (existingBuyer.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            BuyerProfile savedBuyer = buyerProfileRepository.save(buyer);
            return new ResponseEntity<>(savedBuyer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // READ - Get buyer by user ID (NEW ENDPOINT)

    @GetMapping("/user/{userId}")
    public ResponseEntity<BuyerProfile> getBuyerByUserId(@PathVariable("userId") Long userId) {
        try {
            Optional<BuyerProfile> buyer = buyerProfileRepository.findByUserId(userId);
            if (buyer.isPresent()) {
                return new ResponseEntity<>(buyer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all buyers
    @GetMapping
    public ResponseEntity<List<BuyerProfile>> getAllBuyers() {
        try {
            List<BuyerProfile> buyers = buyerProfileRepository.findAll();
            if (buyers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(buyers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get buyer by ID
    @GetMapping("/{id}")
    public ResponseEntity<BuyerProfile> getBuyerById(@PathVariable("id") Long id) {
        try {
            Optional<BuyerProfile> buyer = buyerProfileRepository.findById(id);
            if (buyer.isPresent()) {
                return new ResponseEntity<>(buyer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get buyer by email
    @GetMapping("/email/{email}")
    public ResponseEntity<BuyerProfile> getBuyerByEmail(@PathVariable("email") String email) {
        try {
            Optional<BuyerProfile> buyer = buyerProfileRepository.findByEmail(email);
            if (buyer.isPresent()) {
                return new ResponseEntity<>(buyer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // // READ - Get buyers by county
    // @GetMapping("/county/{county}")
    // public ResponseEntity<List<BuyerProfile>> getBuyersByCounty(@PathVariable("county") String county) {
    //     try {
    //         List<BuyerProfile> buyers = buyerProfileRepository.findByCountyIgnoreCase(county);
    //         if (buyers.isEmpty()) {
    //             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    //         }
    //         return new ResponseEntity<>(buyers, HttpStatus.OK);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
    // UPDATE - Update buyer by ID
    @PutMapping("/{id}")
    public ResponseEntity<BuyerProfile> updateBuyer(@PathVariable("id") Long id, @RequestBody BuyerProfile buyer) {
        try {
            Optional<BuyerProfile> existingBuyer = buyerProfileRepository.findById(id);
            if (existingBuyer.isPresent()) {
                BuyerProfile buyerToUpdate = existingBuyer.get();

                // Update fields that exist in the model
                buyerToUpdate.setEmail(buyer.getEmail());
                buyerToUpdate.setPhoneNumber(buyer.getPhoneNumber());
                buyerToUpdate.setCity(buyer.getCity());
                buyerToUpdate.setState(buyer.getState());
                buyerToUpdate.setNationalId(buyer.getNationalId());
                buyerToUpdate.setKraPin(buyer.getKraPin());

                BuyerProfile updatedBuyer = buyerProfileRepository.save(buyerToUpdate);
                return new ResponseEntity<>(updatedBuyer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update buyer by ID
    @PatchMapping("/{id}")
    public ResponseEntity<BuyerProfile> partialUpdateBuyer(@PathVariable("id") Long id, @RequestBody BuyerProfile buyer) {
        try {
            Optional<BuyerProfile> existingBuyer = buyerProfileRepository.findById(id);
            if (existingBuyer.isPresent()) {
                BuyerProfile buyerToUpdate = existingBuyer.get();

                // Update only non-null fields
                if (buyer.getEmail() != null) {
                    buyerToUpdate.setEmail(buyer.getEmail());
                }
                if (buyer.getPhoneNumber() != null) {
                    buyerToUpdate.setPhoneNumber(buyer.getPhoneNumber());
                }
                if (buyer.getCity() != null) {
                    buyerToUpdate.setCity(buyer.getCity());
                }
                if (buyer.getState() != null) {
                    buyerToUpdate.setState(buyer.getState());
                }
                if (buyer.getNationalId() != null) {
                    buyerToUpdate.setNationalId(buyer.getNationalId());
                }
                if (buyer.getKraPin() != null) {
                    buyerToUpdate.setKraPin(buyer.getKraPin());
                }

                BuyerProfile updatedBuyer = buyerProfileRepository.save(buyerToUpdate);
                return new ResponseEntity<>(updatedBuyer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete buyer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBuyer(@PathVariable("id") Long id) {
        try {
            Optional<BuyerProfile> buyer = buyerProfileRepository.findById(id);
            if (buyer.isPresent()) {
                buyerProfileRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Utility endpoint - Get buyer count
    @GetMapping("/count")
    public ResponseEntity<Long> getBuyerCount() {
        try {
            long count = buyerProfileRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
