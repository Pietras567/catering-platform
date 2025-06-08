package dto;

import entities.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String eventType;
    private LocalDate date;
    private LocalTime time;
    private Integer peopleCount;
    private BigDecimal totalCost;
    private EventStatus status;
    private String clientUsername;
    private String clientEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EventDishResponse> dishes;
}

