package com.example.backend_pfm.repositories;


import com.example.backend_pfm.beans.Don;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonRepository extends JpaRepository<Don, Long> {
    List<Don> findByIdUtilisateur(Long idUtilisateur);
}
