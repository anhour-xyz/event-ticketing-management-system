package com.anhour.tickets.services.impl;

import com.anhour.tickets.domain.CreateEventRequest;
import com.anhour.tickets.domain.entities.Event;
import com.anhour.tickets.domain.entities.User;
import com.anhour.tickets.repositories.EventRepository;
import com.anhour.tickets.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void createsEventWhenOrganizerCollectionIsNull() {
        UUID organizerId = UUID.randomUUID();
        User organizer = User.builder().id(organizerId).build();
        CreateEventRequest request = new CreateEventRequest();

        when(userRepository.findById(organizerId)).thenReturn(Optional.of(organizer));
        when(eventRepository.save(any(Event.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EventServiceImpl service = new EventServiceImpl(eventRepository, userRepository);

        Event createdEvent = service.createEvent(organizerId, request);

        assertSame(organizer, createdEvent.getOrganizer());
    }
}
