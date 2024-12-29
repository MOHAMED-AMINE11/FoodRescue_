package com.example.backend_pfm.controller;

import com.example.backend_pfm.beans.Donations;
import com.example.backend_pfm.repository.DonationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/donations")
public class DonationsController {

    private final DonationsRepository donationsRepository;

    @Autowired
    public DonationsController(DonationsRepository donationsRepository) {
        this.donationsRepository = donationsRepository;
    }

    // Créer un nouveau don
    @PostMapping
    public ResponseEntity<Donations> createDonation(
            @RequestPart("donation") Donations donation,
            @RequestPart("image") MultipartFile image) {


        try {

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get("C:/xampp/htdocs/uploads/" + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());
            donation.setFoodImagePreview("http://192.168.137.1/uploads/" + fileName);
            // Set the image path in the donation entity

            // Save the donation to the repository
            System.out.println(donation.getUniter());
            Donations savedDonation = donationsRepository.save(donation);

            return new ResponseEntity<>(savedDonation, HttpStatus.CREATED);

        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Obtenir tous les dons
    @GetMapping
    public List<Donations> getAllDonations() {
        return donationsRepository.findAll();
    }

    // Obtenir un don par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Donations> getDonationById(@PathVariable("id") int id) {
        Optional<Donations> donation = donationsRepository.findById(id);
        return donation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Obtenir des dons par type d'aliment
    @GetMapping("/foodType/{foodTypeGroup}")
    public List<Donations> getDonationsByFoodTypeGroup(@PathVariable("foodTypeGroup") String foodTypeGroup) {
        return donationsRepository.findByFoodTypeGroup(foodTypeGroup);
    }

    // Obtenir des dons en fonction de l'état de fermeture
    @GetMapping("/closed/{isClosed}")
    public List<Donations> getDonationsByClosedStatus(@PathVariable("isClosed") boolean isClosed) {
        return donationsRepository.findByIsClosed(isClosed);
    }

    // Mettre à jour un don
    @PutMapping("/{id}")
    public ResponseEntity<Donations> updateDonation(@PathVariable("id") int id, @RequestBody Donations updatedDonation) {
        Optional<Donations> existingDonation = donationsRepository.findById(id);
        if (existingDonation.isPresent()) {
            updatedDonation.setIdDonation(id);
            Donations savedDonation = donationsRepository.save(updatedDonation);
            return ResponseEntity.ok(savedDonation);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Supprimer un don
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDonation(@PathVariable("id") int id) {
        Optional<Donations> donation = donationsRepository.findById(id);
        if (donation.isPresent()) {
            donationsRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("/user/{userId}")
    public List<Donations> getDonationsByUserId(@PathVariable("userId") int userId) {
        return donationsRepository.findByIdDonner(userId);

}
}
