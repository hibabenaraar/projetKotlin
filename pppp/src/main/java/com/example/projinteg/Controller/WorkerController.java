package com.example.projinteg.Controller;

import com.example.projinteg.entity.Worker;
import com.example.projinteg.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    // Create a new worker
    @PostMapping
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) {
        Worker savedWorker = workerService.createWorker(worker);
        return new ResponseEntity<>(savedWorker, HttpStatus.CREATED);
    }

    // Fetch all workers
    @GetMapping
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workers = workerService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }

    // Fetch workers by service
    @GetMapping("/by-service")
    public ResponseEntity<List<Worker>> getWorkersByService(@RequestParam String service) {
        List<Worker> workers = workerService.getWorkersByService(service);
        return ResponseEntity.ok(workers);
    }

    // Update an existing worker
    @PutMapping("/{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable Long id, @RequestBody Worker workerDetails) {
        Worker updatedWorker = workerService.updateWorker(id, workerDetails);
        return ResponseEntity.ok(updatedWorker);
    }

    // Delete a worker
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
