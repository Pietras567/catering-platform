package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestResponse {

    private Long id;
    private String eventType;
    private Integer peopleCount;
    private LocalDate date;
    private LocalTime time;
    private String preferences;
    private BigDecimal budget;
    private String dietaryPreferences;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
