package com.anhour.tickets.services.impl;

import org.springframework.stereotype.Service;

import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.entities.TicketStatusEnum;
import com.anhour.tickets.domain.entities.TicketType;
import com.anhour.tickets.exceptions.TicketTypeNotFoundException;
import com.anhour.tickets.exceptions.TicketsSoldOutException;
import com.anhour.tickets.exceptions.UserNotFoundException;
import com.anhour.tickets.repositories.TicketRepository;
import com.anhour.tickets.repositories.TicketTypeRepository;
import com.anhour.tickets.repositories.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import com.anhour.tickets.services.QrCodeService;
import com.anhour.tickets.services.TicketTypeService;
import com.anhour.tickets.domain.entities.User;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
    
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
            String.format("User with ID %s was not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
            .orElseThrow(() -> new TicketTypeNotFoundException(
                String.format("Ticket type with ID %s was not found", ticketTypeId)
            ));

        int purchasedTickets = ticketRepository.countByTicketTypeIdAndStatus(
            ticketType.getId(), TicketStatusEnum.PURCHASED
        );
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (totalAvailable == null || purchasedTickets >= totalAvailable){
            throw new TicketsSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);
        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return savedTicket;

}
}
