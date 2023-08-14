package com.example.fly_abroad.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FA_tickets")
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Flight flight;

    @Enumerated(value = EnumType.STRING)
    private TicketClass ticketClass;

    private BigDecimal price;

    private Currency currency;

    private int totalQuantity;

    private int bookedQuantity;

    private int unbookedQuantity;
}
