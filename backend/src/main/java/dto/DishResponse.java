package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long dishTypeId;
    private String dishTypeName;
    private String description;
    private byte[] image;
    private Integer energy;
    private Integer protein;
    private Integer fiber;
    private Integer carbohydrates;
    private Integer fats;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
