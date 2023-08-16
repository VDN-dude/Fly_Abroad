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

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User administrator;

    @OneToOne(cascade = CascadeType.ALL)
    private Address officeAddress;

    private String phone;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Flight> flights;

    @Column(length = 2000)
    private String description;

    @Column(length = 4000)
    private String baggageInfo;

    @Column(length = 4000)
    private String childInfo;
}
