package com.anhour.tickets.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;
import com.anhour.tickets.domain.entities.TicketStatusEnum;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetTicketResponseDto {
    
    private UUID id;
    private TicketStatusEnum status;
    private Double price;
    private String description;
    private String eventName;
    private String eventVenue;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;


}
