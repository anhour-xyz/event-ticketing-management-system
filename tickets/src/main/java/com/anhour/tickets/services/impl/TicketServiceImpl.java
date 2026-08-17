package com.anhour.tickets.services.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.QRCodeStatusEnum;
import com.anhour.tickets.domain.entities.TicketStatusEnum;
import com.anhour.tickets.exceptions.TicketNotFoundException;
import com.anhour.tickets.repositories.QrCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.anhour.tickets.repositories.TicketRepository;
import com.anhour.tickets.services.TicketService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService{
    private final TicketRepository ticketRepository;
    private final QrCodeRepository qrCodeRepository;
    @Override
    public Page<Ticket> listTicketsForUser(UUID userId, Pageable pageable){
        return ticketRepository.findByPurchaserId(userId, pageable);
    }

    @Override
    public Optional<Ticket> getTicketForUser(UUID userId, UUID ticketId){
        return ticketRepository.findByIdAndPurchaserId(ticketId, userId);
    }

    @Override
    @Transactional
    public Ticket cancelTicketForUser(UUID userId, UUID ticketId) {
        Ticket ticket = ticketRepository.findByIdAndPurchaserId(ticketId, userId)
            .orElseThrow(TicketNotFoundException::new);

        if (TicketStatusEnum.CANCELLED.equals(ticket.getStatus())) {
            return ticket;
        }

        ticket.setStatus(TicketStatusEnum.CANCELLED);
        qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)
            .ifPresent(qrCode -> {
                qrCode.setStatus(QRCodeStatusEnum.EXPIRED);
                qrCodeRepository.save(qrCode);
            });

        return ticketRepository.save(ticket);
    }

    @Override
    public Optional<QrCode> getQrCodeForUser(UUID userId, UUID ticketId) {
        return qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId);
    }

    @Override
    public long countPurchasedTicketsForUser(UUID userId) {
        return ticketRepository.countByPurchaserIdAndStatus(
            userId, TicketStatusEnum.PURCHASED
        );
    }
    
}
