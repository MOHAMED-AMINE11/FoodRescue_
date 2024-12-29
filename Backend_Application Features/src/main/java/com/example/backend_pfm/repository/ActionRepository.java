package com.example.backend_pfm.repository;

import com.example.backend_pfm.beans.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<Action> findByReceiverId(int receiverId); // Récupérer les actions par receiver
    List<Action> findByDonatorId(int donatorId);  // Récupérer les actions par donator
}
