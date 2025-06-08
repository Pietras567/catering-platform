package dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {

    @NotBlank(message = "The type of event is required")
    @Size(max = 100, message = "The type of event must not exceed 100 characters")
    private String eventType;

    @NotNull(message = "The number of people is required")
    @Min(value = 1, message = "The number of people must be greater than 0")
    @Max(value = 1000, message = "The number of people must not exceed 1000")
    private Integer peopleCount;

    @NotNull(message = "The date of the event is required")
    @Future(message = "The date of the event must be in the future")
    private LocalDate date;

    @NotNull(message = "The time of the event is required")
    private LocalTime time;

    @Size(max = 2000, message = "Menu preferences must not exceed 2000 characters")
    private String preferences;

    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Incorrect budget format")
    private BigDecimal budget;

    @Size(max = 1000, message = "Dietary preferences must not exceed 1000 characters")
    private String dietaryPreferences;
}
