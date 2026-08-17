package com.anhour.tickets.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anhour.tickets.domain.entities.QrCode;

import com.anhour.tickets.domain.entities.QRCodeStatusEnum;
@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID>{
    Optional<QrCode> findByTicketIdAndTicketPurchaserId(UUID ticketId, UUID ticketPurchaserId);
    Optional<QrCode> findByIdAndStatus(UUID id, QRCodeStatusEnum status);
}
