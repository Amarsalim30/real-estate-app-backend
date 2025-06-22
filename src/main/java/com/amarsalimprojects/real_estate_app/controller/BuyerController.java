package com.amarsalimprojects.real_estate_app.controller;

import com.amarsalimprojects.real_estate_app.model.UserManagement.Buyer;
import com.amarsalimprojects.real_estate_app.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buyers")
@CrossOrigin(origins = "*")
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    // CREATE - Add a new buyer
    @PostMapping
    public ResponseEntity<Buyer> createBuyer(@RequestBody Buyer buyer) {
        try {
            // Check if email already exists
            Optional<Buyer> existingBuyer = buyerRepository.findByEmail(buyer.getEmail());
            if (existingBuyer.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            Buyer savedBuyer = buyerRepository.save(buyer);
            return new ResponseEntity<>(savedBuyer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all buyers
    @GetMapping
    public ResponseEntity<List<Buyer>> getAllBuyers() {
        try {
            List<Buyer> buyers = buyerRepository.findAll();
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
    public ResponseEntity<Buyer> getBuyerById(@PathVariable("id") Long id) {
        try {
            Optional<Buyer> buyer = buyerRepository.findById(id);
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
    public ResponseEntity<Buyer> getBuyerByEmail(@PathVariable("email") String email) {
        try {
            Optional<Buyer> buyer = buyerRepository.findByEmail(email);
            if (buyer.isPresent()) {
                return new ResponseEntity<>(buyer.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search buyers by name
    @GetMapping("/search")
    public ResponseEntity<List<Buyer>> searchBuyersByName(@RequestParam("name") String name) {
        try {
            List<Buyer> buyers = buyerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
            if (buyers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(buyers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get buyers by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Buyer>> getBuyersByCity(@PathVariable("city") String city) {
        try {
            List<Buyer> buyers = buyerRepository.findByCityIgnoreCase(city);
            if (buyers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(buyers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get buyers by state
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Buyer>> getBuyersByState(@PathVariable("state") String state) {
        try {
            List<Buyer> buyers = buyerRepository.findByStateIgnoreCase(state);
            if (buyers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(buyers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update buyer by ID
    @PutMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable("id") Long id, @RequestBody Buyer buyer) {
        try {
            Optional<Buyer> existingBuyer = buyerRepository.findById(id);
            if (existingBuyer.isPresent()) {
                Buyer buyerToUpdate = existingBuyer.get();

                // Update fields
                buyerToUpdate.setFirstName(buyer.getFirstName());
                buyerToUpdate.setLastName(buyer.getLastName());
                buyerToUpdate.setEmail(buyer.getEmail());
                buyerToUpdate.setPhone(buyer.getPhone());
                buyerToUpdate.setAddress(buyer.getAddress());
                buyerToUpdate.setCity(buyer.getCity());
                buyerToUpdate.setState(buyer.getState());
                buyerToUpdate.setZipCode(buyer.getZipCode());
                buyerToUpdate.setOccupation(buyer.getOccupation());
                buyerToUpdate.setAnnualIncome(buyer.getAnnualIncome());
                buyerToUpdate.setCreditScore(buyer.getCreditScore());
                buyerToUpdate.setPreferredContactMethod(buyer.getPreferredContactMethod());
                buyerToUpdate.setNotes(buyer.getNotes());

                Buyer updatedBuyer = buyerRepository.save(buyerToUpdate);
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
    public ResponseEntity<Buyer> partialUpdateBuyer(@PathVariable("id") Long id, @RequestBody Buyer buyer) {
        try {
            Optional<Buyer> existingBuyer = buyerRepository.findById(id);
            if (existingBuyer.isPresent()) {
                Buyer buyerToUpdate = existingBuyer.get();

                // Update only non-null fields
                if (buyer.getFirstName() != null) {
                    buyerToUpdate.setFirstName(buyer.getFirstName());
                }
                if (buyer.getLastName() != null) {
                    buyerToUpdate.setLastName(buyer.getLastName());
                }
                if (buyer.getEmail() != null) {
                    buyerToUpdate.setEmail(buyer.getEmail());
                }
                if (buyer.getPhone() != null) {
                    buyerToUpdate.setPhone(buyer.getPhone());
                }
                if (buyer.getAddress() != null) {
                    buyerToUpdate.setAddress(buyer.getAddress());
                }
                if (buyer.getCity() != null) {
                    buyerToUpdate.setCity(buyer.getCity());
                }
                if (buyer.getState() != null) {
                    buyerToUpdate.setState(buyer.getState());
                }
                if (buyer.getZipCode() != null) {
                    buyerToUpdate.setZipCode(buyer.getZipCode());
                }
                if (buyer.getOccupation() != null) {
                    buyerToUpdate.setOccupation(buyer.getOccupation());
                }
                if (buyer.getAnnualIncome() != null) {
                    buyerToUpdate.setAnnualIncome(buyer.getAnnualIncome());
                }
                if (buyer.getCreditScore() != null) {
                    buyerToUpdate.setCreditScore(buyer.getCreditScore());
                }
                if (buyer.getPreferredContactMethod() != null) {
                    buyerToUpdate.setPreferredContactMethod(buyer.getPreferredContactMethod());
                }
                if (buyer.getNotes() != null) {
                    buyerToUpdate.setNotes(buyer.getNotes());
                }

                Buyer updatedBuyer = buyerRepository.save(buyerToUpdate);
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
            Optional<Buyer> buyer = buyerRepository.findById(id);
            if (buyer.isPresent()) {
                buyerRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete all buyers
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllBuyers() {
        try {
            buyerRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional utility endpoint - Get buyer count
    @GetMapping("/count")
    public ResponseEntity<Long> getBuyerCount() {
        try {
            long count = buyerRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
