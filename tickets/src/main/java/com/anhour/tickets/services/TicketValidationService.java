package com.anhour.tickets.services;

import org.springframework.stereotype.Service;
import java.util.UUID;
import com.anhour.tickets.domain.entities.TicketValidation;

@Service
public interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeId);
    TicketValidation validateTicketManually(UUID ticketId);
}
