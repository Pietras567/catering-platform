package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDishResponse {
    private Long dishId;
    private String dishName;
    private String dishSubtitle;
    private BigDecimal dishPrice;
    private byte[] dishImage;
    private String dishCategory;
    private Integer quantity;
    private BigDecimal totalPrice;
}

