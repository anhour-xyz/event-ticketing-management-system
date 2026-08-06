package com.anhour.tickets.repositories;

import com.anhour.tickets.domain.entities.Event;
import com.anhour.tickets.domain.entities.EventStatusEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import java.util.Optional;
import java.util.UUID;


public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);

    Page<Event> findByStatus(EventStatusEnum status, Pageable pagealbe);

    @Query("""
    SELECT e
    FROM Event e
    WHERE e.status = :status
      AND (
        LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        OR LOWER(e.venue) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
      )
    """)
Page<Event> searchEvents(
    @Param("status") EventStatusEnum status,
    @Param("searchTerm") String searchTerm,
    Pageable pageable
);

    Optional<Event> findByIdAndStatus(UUID id, EventStatusEnum status);
}
