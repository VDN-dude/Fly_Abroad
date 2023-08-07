package com.example.fly_abroad.dto;

import com.example.fly_abroad.entity.TicketClass;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTicketDto {

    private TicketClass ticketClass;

    private BigDecimal price;

    private int quantity;
}
