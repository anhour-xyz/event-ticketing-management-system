package com.anhour.tickets.services;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;


public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);
}
