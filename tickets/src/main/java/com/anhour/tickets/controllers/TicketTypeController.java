package com.anhour.tickets.controllers;

import com.anhour.tickets.services.TicketTypeService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/events/{eventId}/ticket-types")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @PostMapping(path = "/{ticketTypeId}/tickets")
    @PreAuthorize("hasAnyRole('ATTENDEE', 'ORGANIZER')")
    public ResponseEntity<Void> purchaseTicket(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable UUID eventId,
        @PathVariable UUID ticketTypeId
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        ticketTypeService.purchaseTicket(userId, ticketTypeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
