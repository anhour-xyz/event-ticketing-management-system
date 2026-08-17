package com.anhour.tickets.services;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.entities.QrCode;

@Service
public interface TicketService {
    
    Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable);
    Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId);
    Ticket cancelTicketForUser(UUID userId, UUID ticketId);
    Optional<QrCode> getQrCodeForUser(UUID userId, UUID ticketId);
    long countPurchasedTicketsForUser(UUID userId);
}
