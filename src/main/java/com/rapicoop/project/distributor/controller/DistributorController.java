package com.rapicoop.project.distributor.controller;

import com.rapicoop.project.distributor.exception.ResourceNotFoundException;
import com.rapicoop.project.distributor.model.Distributor;
import com.rapicoop.project.distributor.repository.DistributorRepository;
import com.rapicoop.project.history.model.History;
import com.rapicoop.project.history.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DistributorController {
    //Set Repositories
    @Autowired
    DistributorRepository distributorRepository;
    @Autowired
    HistoryRepository historyRepository;

    /**
     * get all distributors
     * @return distributors array
     */
    @GetMapping("/distributors")
    public List<Distributor> getAllDistributors() {
        return distributorRepository.findAll();
    }

    /**
     * change the location of the distributor and store the last status and location
     * @param distributorId
     * @param distributorDetails
     * @return updated distributor
     */
    @PostMapping("/distributors/location/{id}")
    public Distributor setDistributorLocation(@PathVariable(value = "id") Long distributorId,
                                              @Valid @RequestBody Distributor distributorDetails) {
        Distributor distributor;
        distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
        History history = new History();
        history.setLatitude(distributorDetails.getLatitude());
        history.setLongitude(distributorDetails.getLongitude());
        history.setDistributor(distributorDetails);
        history.setStatus(distributorDetails.getStatus());
        history.setDeliveryId((long) 1000);
        historyRepository.save(history);
        distributor.setLongitude(distributorDetails.getLongitude());
        distributor.setLatitude(distributorDetails.getLatitude());
        Distributor updatedDistributor = distributorRepository.save(distributor);
        return updatedDistributor;
    }

    /**
     * create a new distributor
     * @param distributor
     * @return created distributor
     */
    @PostMapping("/distributor")
    public Distributor createDistributor(@Valid @RequestBody Distributor distributor) {
        return distributorRepository.save(distributor);
    }

    /**
     * get a single distributor
     * @param distributorId
     * @return distributor or not found error
     */
    @GetMapping("/distributors/{id}")
    public Distributor getDistributorById(@PathVariable(value = "id") Long distributorId) {
        return distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
    }

    /**
     * update the distributor status
     * @param distributorId
     * @param distributorDetails
     * @return
     */
    @PutMapping("/distributors/{id}")
    public Distributor updateDistributorStatus(@PathVariable(value = "id") Long distributorId,
                           @Valid @RequestBody Distributor distributorDetails) {
        Distributor distributor;
        distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));

        distributor.setStatus(distributorDetails.getStatus());

        Distributor updatedDistributor = distributorRepository.save(distributor);
        return updatedDistributor;
    }

    /**
     * delete a distributor from id
     * @param distributorId
     * @return Response
     */
    @DeleteMapping("/distributors/{id}")
    public ResponseEntity<?> deleteDistributor(@PathVariable(value = "id") Long distributorId) {
        Distributor note = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
        distributorRepository.delete(note);
        return ResponseEntity.ok().build();
    }
}