package com.example.loadjpabatch.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;
    private LocalDateTime eventTime;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    public Event(String eventType, LocalDateTime eventTime, User user, Product product) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.user = user;
        this.product = product;
    }
}
