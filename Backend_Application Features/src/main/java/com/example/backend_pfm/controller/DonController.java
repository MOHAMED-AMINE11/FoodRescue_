package com.example.backend_pfm.controller;

import com.example.backend_pfm.beans.Don;
import com.example.backend_pfm.services.DonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dons")
public class DonController {
    @Autowired
    private DonService donService;

    @PostMapping
    public ResponseEntity<Don> createDon(@RequestBody Don don) {
        return ResponseEntity.ok(donService.createDon(don));
    }

    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<List<Don>> getDonsByUtilisateur(@PathVariable Long idUtilisateur) {
        return ResponseEntity.ok(donService.getDonsByUtilisateur(idUtilisateur));
    }
}

