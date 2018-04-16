package com.rapicoop.project.status.controller;

import com.rapicoop.project.distributor.exception.ResourceNotFoundException;
import com.rapicoop.project.status.model.Status;
import com.rapicoop.project.status.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public class StatusController {
    @Autowired
    StatusRepository statusRepository;

    /**
     *  Get All status's
     * @return status's Array
     */
    @GetMapping("/status")
    public List<Status> getAllStatus() {
        return statusRepository.findAll();
    }

    /**
     * Create a new status
     * @param status
     * @return created status
     */
    @PostMapping("/status")
    public Status createStatus(@Valid @RequestBody Status status) {
        return statusRepository.save(status);
    }

    /**
     * Get a single status
     * @param statusId
     * @return status object
     */
    @GetMapping("/status/{id}")
    public Status getStatusById(@PathVariable(value = "id") Long statusId) {
        return statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status", "id", statusId));
    }

    /**
     * Deletes a status
     * @param statusId
     * @return deleted status or not found
     */
    @DeleteMapping("/status/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable(value = "id") Long statusId) {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", statusId));
        statusRepository.delete(status);
        return ResponseEntity.ok().build();
    }
}

