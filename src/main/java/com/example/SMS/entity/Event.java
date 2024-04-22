package com.example.SMS.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "posted_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp postedDate;

    @Column(name = "date_time")
    private Timestamp dateTime;

    private Double price;

    private String title;

    @Column(name = "address")
    private String address;

    @Column(name = "available_seats")
    private int availableSeats;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users;
}