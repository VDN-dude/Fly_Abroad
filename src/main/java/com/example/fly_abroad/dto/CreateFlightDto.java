package com.example.fly_abroad.dto;

import com.example.fly_abroad.entity.TicketClass;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateFlightDto {

    private String fromName;

    private String fromCountry;

    private String fromCity;

    private String toName;

    private String toCountry;

    private String toCity;

    private String departure;

    private String arrival;

    private List<CreateTicketDto> tickets = new ArrayList<>();

    public CreateFlightDto() {
        for (TicketClass value : TicketClass.values()) {
            CreateTicketDto createTicketDto = new CreateTicketDto();
            createTicketDto.setTicketClass(value);
            tickets.add(createTicketDto);
        }
    }
}
