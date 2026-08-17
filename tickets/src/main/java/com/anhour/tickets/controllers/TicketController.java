package com.anhour.tickets.controllers;

import com.anhour.tickets.domain.dtos.GetTicketResponseDto;
import com.anhour.tickets.domain.dtos.ListTicketResponseDto;
import com.anhour.tickets.domain.entities.QrCode;
import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.mappers.TicketMapper;
import com.anhour.tickets.services.TicketService;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public ResponseEntity<Page<ListTicketResponseDto>> listTickets(
        @AuthenticationPrincipal Jwt jwt,
        Pageable pageable
    ) {
        UUID userId = parseUserId(jwt);
        Page<Ticket> tickets = ticketService.listTicketsForUser(userId, pageable);
        return ResponseEntity.ok(tickets.map(ticketMapper::toListTicketResponseDto));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countPurchasedTickets(
        @AuthenticationPrincipal Jwt jwt
    ) {
        long count = ticketService.countPurchasedTicketsForUser(parseUserId(jwt));
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<GetTicketResponseDto> getTicket(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID ticketId
    ) {
        return ticketService.getTicketForUser(parseUserId(jwt), ticketId)
            .map(ticketMapper::toGetTicketResponseDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{ticketId}/qr-codes", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getTicketQrCode(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID ticketId
    ) {
        return ticketService.getQrCodeForUser(parseUserId(jwt), ticketId)
            .map(QrCode::getValue)
            .map(Base64.getDecoder()::decode)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> cancelTicket(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID ticketId
    ) {
        ticketService.cancelTicketForUser(parseUserId(jwt), ticketId);
        return ResponseEntity.noContent().build();
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
