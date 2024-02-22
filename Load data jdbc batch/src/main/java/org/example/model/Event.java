package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Event {

    private String eventType;
    private LocalDateTime eventTime;
    private Long userId;

    private Long productId;

    public Event(){}

    public Event(String typeEvent, LocalDateTime eventTime, Long userId, Long productId) {
        this.eventType = typeEvent;
        this.eventTime = eventTime;
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(userId, event.userId) && Objects.equals(eventTime, event.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventTime);
    }
}
