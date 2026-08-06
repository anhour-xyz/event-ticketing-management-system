package com.anhour.tickets.services.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import com.anhour.tickets.domain.entities.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.anhour.tickets.repositories.TicketRepository;
import com.anhour.tickets.services.TicketService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService{
    

    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable){
        return ticketRepository.findByPurchaserId(userId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId){
        return ticketRepository.findByIdAndPurchaserId(ticketId, userId);
    }
    
}
