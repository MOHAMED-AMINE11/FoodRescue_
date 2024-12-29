package com.example.backend_pfm.beans;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // Relation clé étrangère vers la table User
    @JoinColumn(name = "donator_id", nullable = false)
    private User donator;

    @ManyToOne // Relation clé étrangère vers la table User
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    private float altitude;

    private float longitude;

    private boolean isValid;

    private String description;
}
