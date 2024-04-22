package com.example.SMS.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reserved_by")
    private User reservedBy;

    @Column(name = "posted_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp postedDate;

    @Column(name = "date_time")
    private Timestamp dateTime;

    private Double price;
    
    @Column(name = "available_seats")
    private int availableSeats;

    private String title;

    @Column(name = "from_address")
    private String from;

    @Column(name = "to_address")
    private String to;

    private String description;

    private String status;

}