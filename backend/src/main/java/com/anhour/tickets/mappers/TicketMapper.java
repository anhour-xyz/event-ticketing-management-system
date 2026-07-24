package com.anhour.tickets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.anhour.tickets.domain.entities.Ticket;
import com.anhour.tickets.domain.dtos.GetTicketResponseDto;
import com.anhour.tickets.domain.dtos.ListTicketResponseDto;
import com.anhour.tickets.domain.dtos.ListTicketTicketTypeResponseDto;
import com.anhour.tickets.domain.entities.TicketType;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {
    
    ListTicketTicketTypeResponseDto toListTicketTypeResponseDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto (Ticket ticket);

    @Mapping(target = "price", source = "ticket.ticketType.price")
    @Mapping(target = "description", source = "ticket.ticketType.description")
    @Mapping(target = "eventName", source = "ticket.ticketType.event.name")
    @Mapping(target = "eventVenue", source = "ticket.ticketType.event.venue")
    @Mapping(target = "eventStart", source = "ticket.ticketType.event.start")
    @Mapping(target = "eventEnd", source = "ticket.ticketType.event.end")
    GetTicketResponseDto toGetTicketResponseDto(Ticket ticket);

}
