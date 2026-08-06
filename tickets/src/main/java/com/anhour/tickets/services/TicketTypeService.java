package com.anhour.tickets.services;

import java.util.UUID;
import com.anhour.tickets.domain.entities.Ticket;
import org.springframework.stereotype.Service;

@Service
public interface TicketTypeService {
    
    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
