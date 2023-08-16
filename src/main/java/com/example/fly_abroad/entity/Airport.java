package com.example.fly_abroad.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FA_airport")
@Builder
@ToString
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @Column(length = 2000)
    private String description;
}
