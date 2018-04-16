package com.rapicoop.project.history.controller;

import com.rapicoop.project.distributor.exception.ResourceNotFoundException;
import com.rapicoop.project.history.model.History;
import com.rapicoop.project.history.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HistoryController {
    @Autowired
    HistoryRepository historyRepository;

    /**
     *  Get All histories
     * @return histories Array
     */
    @GetMapping("/histories")
    public List<History> getAllHistories() {
        return historyRepository.findAll();
    }

    /**
     * Create a new history
     * @param history
     * @return created History
     */
    @PostMapping("/history")
        public History createHistory(@Valid @RequestBody History history) {
        return historyRepository.save(history);
    }

    /**
     * Get a single history
     * @param historyId
     * @return history object
     */
    @GetMapping("/history/{id}")
    public History getHistoryById(@PathVariable(value = "id") Long historyId) {
        return historyRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", historyId));
    }

    /**
     * Deletes a history
     * @param historyId
     * @return deleted history or not found
     */
    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteHistory(@PathVariable(value = "id") Long historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", historyId));
        historyRepository.delete(history);
        return ResponseEntity.ok().build();
    }
}