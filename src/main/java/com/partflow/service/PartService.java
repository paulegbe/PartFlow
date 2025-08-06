package com.partflow.service;

import com.partflow.model.Part;
import com.partflow.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

    public List<Part> getAllParts(){
        return partRepository.findAll();
    }

    public Optional<Part> getPartById(Long id){
        return partRepository.findById(id);
    }

    public Part savePart(Part part){
        return partRepository.save(part);
    }

    public void deletePart(Long id){
        partRepository.deleteById(id);
    }

    public List<Part> checkLowStockParts() {
        return partRepository.findAll().stream()
                .filter(p -> p.getQuantity() < p.getRestockThreshold())
                .collect(Collectors.toList());
    }

    public void autoRestock() {
        List<Part> lowStockParts = checkLowStockParts();
        for (Part part : lowStockParts) {
            part.setQuantity(part.getQuantity() + part.getRestockAmount());
            partRepository.save(part);
            // optionally log or create a restock record
        }
    }



}
