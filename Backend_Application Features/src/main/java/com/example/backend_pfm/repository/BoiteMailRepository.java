package com.example.backend_pfm.repository;

import com.example.backend_pfm.beans.BoiteMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoiteMailRepository extends JpaRepository<BoiteMail, Integer> {

    // Récupérer toutes les demandes d'un donneur spécifique
    List<BoiteMail> findByDonor_Id(int donorId);

    // Récupérer toutes les demandes d'un bénéficiaire spécifique
    List<BoiteMail> findByBeneficiary_Id(int beneficiaryId);

    // Récupérer les demandes liées à un don spécifique
    List<BoiteMail> findByDonation_IdDonation(int idDonation);

    // Récupérer les demandes actives d'un bénéficiaire spécifique
    List<BoiteMail> findByBeneficiary_IdAndIsActive(int beneficiaryId, boolean isActive);



}
