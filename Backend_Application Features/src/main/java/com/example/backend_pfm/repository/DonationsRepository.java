package com.example.backend_pfm.repository;

import com.example.backend_pfm.beans.Donations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationsRepository extends JpaRepository<Donations, Integer> {
    List<Donations> findByFoodTypeGroup(String foodTypeGroup);
    List<Donations> findByIsClosed(boolean isClosed);

    List<Donations> findByIdDonner(int userId);
}
