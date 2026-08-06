package com.anhour.tickets.controllers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.anhour.tickets.domain.dtos.TicketValidationResponseDto;
import com.anhour.tickets.domain.entities.TicketValidationMethod;
import com.anhour.tickets.mappers.TicketValidationMapper;
import com.anhour.tickets.services.TicketValidationService;
import com.anhour.tickets.domain.entities.TicketValidation;
import lombok.AllArgsConstructor;
import com.anhour.tickets.domain.dtos.TicketValidationRequestDto;

@RestController
@RequestMapping(path = "/api/v1/ticket-validations")
@AllArgsConstructor
public class TicketValidationController {
    
    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(@RequestBody TicketValidationRequestDto ticketValidationRequestDto){
        TicketValidationMethod method = resolveValidationMethod(ticketValidationRequestDto);
        TicketValidation ticketValidation;
        if(TicketValidationMethod.MANUAL.equals(method)){
            ticketValidation = ticketValidationService.validateTicketManually(
                ticketValidationRequestDto.getId()
            );
        }else{
            ticketValidation = ticketValidationService.validateTicketByQrCode(
                ticketValidationRequestDto.getId()
            );
        }
        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto(ticketValidation));

    }

    private TicketValidationMethod resolveValidationMethod(TicketValidationRequestDto ticketValidationRequestDto) {
        for (String methodName : new String[]{"getValidationMethod", "getMethod", "getTicketValidationMethod"}) {
            try {
                Object value = ticketValidationRequestDto.getClass().getMethod(methodName).invoke(ticketValidationRequestDto);
                if (value instanceof TicketValidationMethod) {
                    return (TicketValidationMethod) value;
                }
                if (value instanceof String) {
                    return TicketValidationMethod.valueOf(((String) value).toUpperCase());
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
        throw new IllegalArgumentException("Unable to resolve validation method from request");
    }
}
