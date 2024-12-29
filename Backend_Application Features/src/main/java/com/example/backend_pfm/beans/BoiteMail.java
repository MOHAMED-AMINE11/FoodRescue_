package com.example.backend_pfm.beans;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "boite_mail")
public class BoiteMail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mail")
    private int idMail;

    // Relation avec le bénéficiaire
    @ManyToOne
    @JoinColumn(name = "id_beneficiary", nullable = false)
    private User beneficiary;

    // Relation avec le donneur
    @ManyToOne
    @JoinColumn(name = "id_donor", nullable = false)
    private User donor;

    // Relation avec le don
    @ManyToOne
    @JoinColumn(name = "id_donation", nullable = false)
    private Donations donation;

    @Column(name = "message", length = 500)
    private String message; // Optionnel : Message du bénéficiaire

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isActive = true; // Nouveau champ avec une valeur par défaut de `1`

    // Getters et Setters
    public int getIdMail() {
        return idMail;
    }

    public void setIdMail(int idMail) {
        this.idMail = idMail;
    }

    public User getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(User beneficiary) {
        this.beneficiary = beneficiary;
    }

    public User getDonor() {
        return donor;
    }

    public void setDonor(User donor) {
        this.donor = donor;
    }

    public Donations getDonation() {
        return donation;
    }

    public void setDonation(Donations donation) {
        this.donation = donation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
