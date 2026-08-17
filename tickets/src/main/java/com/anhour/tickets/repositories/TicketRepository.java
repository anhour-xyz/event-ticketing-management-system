package com.anhour.tickets.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.UUID;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.entities.TicketStatusEnum;

public interface TicketRepository extends JpaRepository<Ticket, UUID>{

    int countByTicketTypeId(UUID ticketTypeId);

    int countByTicketTypeIdAndStatus(UUID ticketTypeId, TicketStatusEnum status);

    Page<Ticket> findByPurchaserId(UUID purchaserId, Pageable pageable);

    Optional<Ticket> findByIdAndPurchaserId(UUID id, UUID purchaserId);

    long countByPurchaserIdAndStatus(UUID purchaserId, TicketStatusEnum status);
    
}
