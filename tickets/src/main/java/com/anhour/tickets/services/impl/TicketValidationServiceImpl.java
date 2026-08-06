package com.anhour.tickets.services.impl;
import com.anhour.tickets.domain.entities.QRCodeStatusEnum;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.entities.TicketValidation;
import com.anhour.tickets.domain.entities.TicketValidationMethod;
import com.anhour.tickets.domain.entities.TicketValidationStatusEnum;
import com.anhour.tickets.exceptions.QrCodeNotFoundException;
import com.anhour.tickets.exceptions.TicketNotFoundException;
import com.anhour.tickets.repositories.QrCodeRepository;
import com.anhour.tickets.repositories.TicketRepository;
import com.anhour.tickets.repositories.TicketValidationRepository;
import lombok.AllArgsConstructor;
import com.anhour.tickets.services.TicketValidationService;

@Service
@AllArgsConstructor
public class TicketValidationServiceImpl implements TicketValidationService{
    
    private final TicketValidationRepository ticketValidationRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;

    @Override
  public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
    QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QRCodeStatusEnum.ACTIVE)
        .orElseThrow(() -> new QrCodeNotFoundException(
            String.format(
                "QR Code with ID %s was not found", qrCodeId
            )
        ));

    Ticket ticket = qrCode.getTicket();

    return validateTicket(ticket, TicketValidationMethod.QR_SCAN);
  }

  private TicketValidation validateTicket(Ticket ticket,
      TicketValidationMethod ticketValidationMethod) {
    TicketValidation ticketValidation = new TicketValidation();
    ticketValidation.setTicket(ticket);
    ticketValidation.setValidationMethod(ticketValidationMethod);

    TicketValidationStatusEnum ticketValidationStatus = ticket.getValidations().stream()
        .filter(v -> TicketValidationStatusEnum.VALID.equals(v.getStatus()))
        .findFirst()
        .map(v -> TicketValidationStatusEnum.INVALID)
        .orElse(TicketValidationStatusEnum.VALID);

    ticketValidation.setStatus(ticketValidationStatus);

    return ticketValidationRepository.save(ticketValidation);
  }

  @Override
  public TicketValidation validateTicketManually(UUID ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId)
        .orElseThrow(TicketNotFoundException::new);
    return validateTicket(ticket, TicketValidationMethod.MANUAL);
  }
        
}
