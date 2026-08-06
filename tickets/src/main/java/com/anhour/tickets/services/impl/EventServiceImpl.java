package com.anhour.tickets.services.impl;

import com.anhour.tickets.domain.CreateEventRequest;
import com.anhour.tickets.domain.CreateTicketTypeRequest;
import com.anhour.tickets.domain.UpdateEventRequest;
import com.anhour.tickets.domain.UpdateTicketTypeRequest;
import com.anhour.tickets.domain.entities.Event;
import com.anhour.tickets.domain.entities.EventStatusEnum;
import com.anhour.tickets.domain.entities.TicketType;
import com.anhour.tickets.domain.entities.User;
import com.anhour.tickets.exceptions.EventNotFoundException;
import com.anhour.tickets.exceptions.EventUpdateException;
import com.anhour.tickets.exceptions.TicketNotFoundException;
import com.anhour.tickets.exceptions.UserNotFoundException;
import com.anhour.tickets.repositories.EventRepository;
import com.anhour.tickets.repositories.UserRepository;
import com.anhour.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest request) {
        User organizer = userRepository.findById(organizerId)
            .orElseThrow(() -> new UserNotFoundException(
                String.format("User with ID '%s' not found", organizerId)
            ));

        Event event = new Event();
        event.setName(request.getName());
        event.setStart(request.getStart());
        event.setEnd(request.getEnd());
        event.setVenue(request.getVenue());
        event.setSalesStart(request.getSalesStart());
        event.setSalesEnd(request.getSalesEnd());
        event.setStatus(request.getStatus());
        event.setOrganizer(organizer);

        List<CreateTicketTypeRequest> ticketTypeRequests =
            request.getTicketTypes() == null ? List.of() : request.getTicketTypes();
        List<TicketType> ticketTypes = ticketTypeRequests.stream()
            .map(ticketTypeRequest -> createTicketType(ticketTypeRequest, event))
            .collect(Collectors.toCollection(ArrayList::new));
        event.setTicketTypes(ticketTypes);

        return eventRepository.save(event);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(
        UUID organizerId,
        UUID id,
        UpdateEventRequest request
    ) {
        if (request.getId() == null) {
            throw new EventUpdateException("Event ID cannot be null");
        }
        if (!id.equals(request.getId())) {
            throw new EventUpdateException("Cannot update the ID of an event");
        }

        Event existingEvent = eventRepository.findByIdAndOrganizerId(id, organizerId)
            .orElseThrow(() -> new EventNotFoundException(
                String.format("Event with ID '%s' does not exist", id)
            ));

        existingEvent.setName(request.getName());
        existingEvent.setStart(request.getStart());
        existingEvent.setEnd(request.getEnd());
        existingEvent.setVenue(request.getVenue());
        existingEvent.setSalesStart(request.getSalesStart());
        existingEvent.setSalesEnd(request.getSalesEnd());
        existingEvent.setStatus(request.getStatus());

        List<UpdateTicketTypeRequest> requestedTicketTypes =
            request.getTicketTypes() == null ? List.of() : request.getTicketTypes();
        Set<UUID> requestedIds = requestedTicketTypes.stream()
            .map(UpdateTicketTypeRequest::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        existingEvent.getTicketTypes()
            .removeIf(ticketType -> !requestedIds.contains(ticketType.getId()));

        Map<UUID, TicketType> existingTicketTypesById =
            existingEvent.getTicketTypes().stream()
                .filter(ticketType -> ticketType.getId() != null)
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketTypeRequest : requestedTicketTypes) {
            if (ticketTypeRequest.getId() == null) {
                TicketType ticketType = createTicketType(ticketTypeRequest, existingEvent);
                existingEvent.getTicketTypes().add(ticketType);
                continue;
            }

            TicketType ticketType = existingTicketTypesById.get(ticketTypeRequest.getId());
            if (ticketType == null) {
                throw new TicketNotFoundException(String.format(
                    "Ticket type with ID '%s' does not exist",
                    ticketTypeRequest.getId()
                ));
            }
            copyTicketTypeFields(ticketTypeRequest, ticketType);
        }

        return eventRepository.save(existingEvent);
    }

    private TicketType createTicketType(
        CreateTicketTypeRequest request,
        Event event
    ) {
        TicketType ticketType = new TicketType();
        copyTicketTypeFields(request, ticketType);
        ticketType.setEvent(event);
        return ticketType;
    }

    private TicketType createTicketType(
        UpdateTicketTypeRequest request,
        Event event
    ) {
        TicketType ticketType = new TicketType();
        copyTicketTypeFields(request, ticketType);
        ticketType.setEvent(event);
        return ticketType;
    }

    private void copyTicketTypeFields(
        CreateTicketTypeRequest source,
        TicketType target
    ) {
        target.setName(source.getName());
        target.setPrice(source.getPrice());
        target.setDescription(source.getDescription());
        target.setTotalAvailable(source.getTotalAvailable());
    }

    private void copyTicketTypeFields(
        UpdateTicketTypeRequest source,
        TicketType target
    ) {
        target.setName(source.getName());
        target.setPrice(source.getPrice());
        target.setDescription(source.getDescription());
        target.setTotalAvailable(source.getTotalAvailable());
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID id) {
        getEventForOrganizer(organizerId, id).ifPresent(eventRepository::delete);  
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
        return eventRepository.findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        
        return eventRepository.searchEvents(EventStatusEnum.PUBLISHED,query.trim(),pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID id) {
    
        return eventRepository.findByIdAndStatus(id, EventStatusEnum.PUBLISHED);
    }

    

    
}
