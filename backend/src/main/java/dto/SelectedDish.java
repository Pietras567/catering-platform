package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedDish {
    private Long id;
    private String name;
    private String subtitle;
    private BigDecimal price;
    private String image;
    private String category;
    private String categoryName;
    private Integer quantity;
}

