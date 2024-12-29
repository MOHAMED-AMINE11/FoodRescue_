package com.example.backend_pfm.services;

import com.example.backend_pfm.beans.Association;
import com.example.backend_pfm.exceptions.ResourceNotFoundException;
import com.example.backend_pfm.repositories.AssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociationService {
    @Autowired
    private AssociationRepository associationRepository;

    public List<Association> getAllAssociations() {
        return associationRepository.findAll();
    }

    public Association getAssociationById(Long id) {
        return associationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Association non trouvée avec l'id: " + id));
    }
    public Association createAssociation(Association association) {
        return associationRepository.save(association);
    }

}
