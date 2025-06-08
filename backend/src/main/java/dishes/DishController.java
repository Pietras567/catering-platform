
package dishes;

import dto.DishResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Slf4j
public class DishController {

    private final DishService dishService;

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getDish(@PathVariable Long id) {
        log.info("Request to get dish with ID: {}", id);

        DishResponse response = dishService.getDishById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        log.info("Request to get all dishes");

        List<DishResponse> response = dishService.getAllDishes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<DishResponse>> getDishesPaginated(Pageable pageable) {
        log.info("Request to get dishes with pagination");

        Page<DishResponse> response = dishService.getDishesPaginated(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DishResponse>> getAvailableDishes() {
        log.info("Request to get available dishes");

        List<DishResponse> response = dishService.getAvailableDishes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available/paginated")
    public ResponseEntity<Page<DishResponse>> getAvailableDishesPaginated(Pageable pageable) {
        log.info("Request to get available dishes with pagination");

        Page<DishResponse> response = dishService.getAvailableDishesPaginated(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishResponse>> searchDishesByName(@RequestParam String name) {
        log.info("Request to search dishes by name: {}", name);

        List<DishResponse> response = dishService.searchDishesByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{dishTypeId}")
    public ResponseEntity<List<DishResponse>> getDishesByType(@PathVariable Long dishTypeId) {
        log.info("Request to get dishes by type ID: {}", dishTypeId);

        List<DishResponse> response = dishService.getDishesByType(dishTypeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<DishResponse>> getDishesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("Request to get dishes by price range: {} - {}", minPrice, maxPrice);

        List<DishResponse> response = dishService.getDishesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }
}
