package com.anhour.tickets.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anhour.tickets.services.TicketService;
import lombok.AllArgsConstructor;
import com.anhour.tickets.mappers.TicketMapper;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    
}
