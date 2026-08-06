package com.anhour.tickets.services;
import org.springframework.stereotype.Service;

import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;

@Service
public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
}
