package dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishRequest {

    @NotBlank(message = "Dish name is required")
    @Size(min = 2, max = 100, message = "Dish name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price format is invalid")
    private BigDecimal price;

    @NotNull(message = "Dish type ID is required")
    private Long dishTypeId;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private byte[] image;

    @Min(value = 0, message = "Energy cannot be negative")
    private Integer energy;

    @Min(value = 0, message = "Protein cannot be negative")
    private Integer protein;

    @Min(value = 0, message = "Fiber cannot be negative")
    private Integer fiber;

    @Min(value = 0, message = "Carbohydrates cannot be negative")
    private Integer carbohydrates;

    @Min(value = 0, message = "Fats cannot be negative")
    private Integer fats;

    private Boolean isAvailable = true;
}
