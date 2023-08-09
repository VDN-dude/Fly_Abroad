package com.example.fly_abroad.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FA_airline")
@Builder
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    private User administrator;

    @OneToOne(cascade = CascadeType.ALL)
    private Address officeAddress;

    private String phone;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Flight> flights;

    private String description;

    private String baggageInfo;

    private String childInfo;
}
