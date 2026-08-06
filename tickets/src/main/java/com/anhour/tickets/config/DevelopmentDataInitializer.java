package com.anhour.tickets.config;

import com.anhour.tickets.domain.entities.Event;
import com.anhour.tickets.domain.entities.EventStatusEnum;
import com.anhour.tickets.domain.entities.TicketType;
import com.anhour.tickets.repositories.EventRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevelopmentDataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (eventRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        eventRepository.saveAll(List.of(
            createEvent(
                "Summer Music Festival",
                "Riverside Arena",
                now.plusDays(30),
                now.plusDays(30).plusHours(5),
                List.of(
                    ticket("General Admission", 29.99, 500),
                    ticket("VIP", 89.99, 80)
                )
            ),
            createEvent(
                "Technology Conference 2026",
                "City Convention Centre",
                now.plusDays(45),
                now.plusDays(47),
                List.of(
                    ticket("Conference Pass", 149.00, 300),
                    ticket("Student Pass", 59.00, 100)
                )
            ),
            createEvent(
                "International Food Fair",
                "Central Exhibition Park",
                now.plusDays(60),
                now.plusDays(60).plusHours(8),
                List.of(ticket("Entry Pass", 15.00, 1000))
            ),
            createEvent(
                "Championship Final",
                "National Stadium",
                now.plusDays(75),
                now.plusDays(75).plusHours(3),
                List.of(
                    ticket("Standard Seat", 45.00, 600),
                    ticket("Premium Seat", 120.00, 120)
                )
            )
        ));
    }

    private Event createEvent(
        String name,
        String venue,
        LocalDateTime start,
        LocalDateTime end,
        List<TicketType> ticketTypes
    ) {
        LocalDateTime now = LocalDateTime.now();
        Event event = new Event();
        event.setName(name);
        event.setVenue(venue);
        event.setStart(start);
        event.setEnd(end);
        event.setSalesStart(now.minusDays(1));
        event.setSalesEnd(start.minusHours(1));
        event.setStatus(EventStatusEnum.PUBLISHED);
        event.setCreatedAt(now);
        event.setUpdatedAt(now);
        event.setAttendees(new ArrayList<>());
        event.setStaff(new ArrayList<>());
        event.setTicketTypes(new ArrayList<>(ticketTypes));
        event.getTicketTypes().forEach(ticketType -> ticketType.setEvent(event));
        return event;
    }

    private TicketType ticket(String name, double price, int totalAvailable) {
        LocalDateTime now = LocalDateTime.now();
        TicketType ticketType = new TicketType();
        ticketType.setName(name);
        ticketType.setPrice(price);
        ticketType.setDescription("Preset development ticket type");
        ticketType.setTotalAvailable(totalAvailable);
        ticketType.setTickets(new ArrayList<>());
        ticketType.setCreatedAt(now);
        ticketType.setUpdatedAt(now);
        return ticketType;
    }
}
