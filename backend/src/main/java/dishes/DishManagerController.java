package dishes;

import dto.DishRequest;
import dto.DishResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dishes")
@RequiredArgsConstructor
@Slf4j
public class DishManagerController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<DishResponse> createDish(@Valid @RequestBody DishRequest dishRequestDto) {
        log.info("Manager request to create new dish: {}", dishRequestDto.getName());

        DishResponse response = dishService.createDish(dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> updateDish(@PathVariable Long id, @Valid @RequestBody DishRequest dishRequestDto) {
        log.info("Manager request to update dish with ID: {}", id);

        DishResponse response = dishService.updateDish(id, dishRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        log.info("Manager request to delete dish with ID: {}", id);

        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
