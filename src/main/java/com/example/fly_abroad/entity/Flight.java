package com.example.fly_abroad.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FA_flights")
@Builder
@ToString
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Airline airline;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    private int totalTicketsQuantity;

    private int unbookedTicketsQuantity;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Airport from;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Airport to;

    private LocalDateTime departure;

    private LocalDateTime arrival;

    private Duration duration;
}
