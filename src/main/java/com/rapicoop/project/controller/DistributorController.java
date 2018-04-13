package com.rapicoop.project.controller;

import com.rapicoop.project.exception.ResourceNotFoundException;
import com.rapicoop.project.model.Distributor;
import com.rapicoop.project.repository.DistributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DistributorController {

    @Autowired
    DistributorRepository distributorRepository;

    // Get All Distributors
    @GetMapping("/distributors")
    public List<Distributor> getAllDistributors() {
        return distributorRepository.findAll();
    }
    // Create a new Distributor
    @PostMapping("/distributor")
    public Distributor createDistributor(@Valid @RequestBody Distributor distributor) {
        return distributorRepository.save(distributor);
    }
    // Get a Single Distributor
    @GetMapping("/distributors/{id}")
    public Distributor getDistributorById(@PathVariable(value = "id") Long distributorId) {
        return distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
    }
    // Update a Distributor
    @PutMapping("/distributors/{id}")
    public Distributor updateDistributorStatus(@PathVariable(value = "id") Long distributorId,
                           @Valid @RequestBody Distributor distributorDetails) {
        Distributor distributor;
        distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", distributorId));

        distributor.setStatus(distributorDetails.getStatus());

        Distributor updatedDistributor = distributorRepository.save(distributor);
        return updatedDistributor;
    }
    // Delete a Note
    @DeleteMapping("/distributors/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long distributorId) {
        Distributor note = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", distributorId));
        distributorRepository.delete(note);
        return ResponseEntity.ok().build();
    }
}