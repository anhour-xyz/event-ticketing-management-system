package com.anhour.tickets.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.anhour.tickets.domain.CreateEventRequest;
import com.anhour.tickets.domain.CreateTicketTypeRequest;
import com.anhour.tickets.domain.UpdateEventRequest;
import com.anhour.tickets.domain.UpdateTicketTypeRequest;
import com.anhour.tickets.domain.dtos.CreateEventRequestDto;
import com.anhour.tickets.domain.dtos.CreateEventResponseDto;
import com.anhour.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.anhour.tickets.domain.dtos.GetEventDetailsResponseDto;
import com.anhour.tickets.domain.dtos.GetEventTicketTypesResponseDto;
import com.anhour.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.anhour.tickets.domain.dtos.GetPublishedEventTicketTypesResponseDto;
import com.anhour.tickets.domain.dtos.ListEventResponseDto;
import com.anhour.tickets.domain.dtos.ListEventTicketTypeResponseDto;
import com.anhour.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.anhour.tickets.domain.dtos.UpdateEventRequestDto;
import com.anhour.tickets.domain.dtos.UpdateEventResponseDto;
import com.anhour.tickets.domain.dtos.UpdateTicketTypeRequestDto;
import com.anhour.tickets.domain.dtos.UpdateTicketTypeResponseDto;
import com.anhour.tickets.domain.entities.Event;
import com.anhour.tickets.domain.entities.TicketType;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    GetEventTicketTypesResponseDto toGetEventTicketTypesResponseDto(TicketType ticketType);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

    GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);

    GetPublishedEventTicketTypesResponseDto toGetPublishedEventTicketTypesResponseDto(TicketType ticketType);


}
