package com.example.backend_pfm.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "dons")
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUtilisateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_association")
    private Association association;

    @Column(nullable = false)
    private String typeAliment;

    private Double quantite;
    private String unite;

    @Column(name = "date_peremption")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")  // Format de la date sous forme de chaîne
    private String datePeremption;  // Changement du type de Date à String

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getTypeAliment() {
        return typeAliment;
    }

    public void setTypeAliment(String typeAliment) {
        this.typeAliment = typeAliment;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getDatePeremption() {
        return datePeremption;  // Getter pour la date sous forme de String
    }

    public void setDatePeremption(String datePeremption) {
        this.datePeremption = datePeremption;  // Setter pour la date sous forme de String
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
