package com.example.backend_pfm.controller;

import com.example.backend_pfm.beans.Association;
import com.example.backend_pfm.services.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/associations")
public class AssociationController {
    @Autowired
    private AssociationService associationService;

    @GetMapping
    public ResponseEntity<List<Association>> getAllAssociations() {
        return ResponseEntity.ok(associationService.getAllAssociations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Association> getAssociationById(@PathVariable Long id) {
        return ResponseEntity.ok(associationService.getAssociationById(id));
    }

    @PostMapping
    public ResponseEntity<Association> createAssociation(@RequestBody Association association) {
        Association newAssociation = associationService.createAssociation(association);
        return new ResponseEntity<>(newAssociation, HttpStatus.CREATED);
    }
}
