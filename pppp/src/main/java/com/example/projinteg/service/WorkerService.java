package com.example.projinteg.service;

import com.example.projinteg.entity.Worker;
import com.example.projinteg.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    // Create a new worker
    public Worker createWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    // Fetch all workers
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    // Fetch workers by service
    public List<Worker> getWorkersByService(String service) {
        return workerRepository.findByService(service);
    }

    // Update an existing worker
    public Worker updateWorker(Long id, Worker workerDetails) {
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()) {
            Worker worker = optionalWorker.get();
            worker.setFirstname(workerDetails.getFirstname());
            worker.setLastname(workerDetails.getLastname());
            worker.setOccupation(workerDetails.getOccupation());
            worker.setExpyear(workerDetails.getExpyear());
            worker.setService(workerDetails.getService());
            return workerRepository.save(worker);
        } else {
            throw new RuntimeException("Worker not found with id: " + id);
        }
    }

    // Delete a worker
    public void deleteWorker(Long id) {
        workerRepository.deleteById(id);
    }
}
