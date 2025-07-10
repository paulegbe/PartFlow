package com.partflow.service;

import com.partflow.model.Part;
import com.partflow.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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


}
