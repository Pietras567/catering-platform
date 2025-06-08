package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String eventType;
    private LocalDate date;
    private LocalTime time;
    private Integer peopleCount;
    private List<SelectedDish> selectedDishes;
    private BigDecimal totalCost;
}

