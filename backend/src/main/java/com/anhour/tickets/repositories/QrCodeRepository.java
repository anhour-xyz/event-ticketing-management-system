package com.anhour.tickets.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anhour.tickets.domain.entities.QrCode;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID>{
    
}
