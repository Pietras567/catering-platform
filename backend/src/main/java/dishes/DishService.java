package dishes;

import dto.DishRequest;
import dto.DishResponse;
import entities.Dish;
import entities.DishType;
import repositories.DishRepository;
import repositories.DishTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DishService {

    private final DishRepository dishRepository;
    private final DishTypeRepository dishTypeRepository;

    @Transactional(readOnly = true)
    public DishResponse getDishById(Long id) {
        log.info("Fetching dish with ID: {}", id);

        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with ID: " + id));

        return mapToResponseDto(dish);
    }

    @Transactional(readOnly = true)
    public List<DishResponse> getAllDishes() {
        log.info("Fetching all dishes");

        return dishRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DishResponse> getDishesPaginated(Pageable pageable) {
        log.info("Fetching dishes with pagination");

        return dishRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public List<DishResponse> getAvailableDishes() {
        log.info("Fetching available dishes");

        return dishRepository.findAllAvailableDishes()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DishResponse> getAvailableDishesPaginated(Pageable pageable) {
        log.info("Fetching available dishes with pagination");

        return dishRepository.findAllAvailableDishesPaginated(pageable)
                .map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public List<DishResponse> searchDishesByName(String name) {
        log.info("Searching dishes by name: {}", name);

        return dishRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DishResponse> getDishesByType(Long dishTypeId) {
        log.info("Fetching dishes by type ID: {}", dishTypeId);

        return dishRepository.findByDishTypeId(dishTypeId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DishResponse> getDishesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching dishes by price range: {} - {}", minPrice, maxPrice);

        return dishRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public DishResponse createDish(DishRequest dto) {
        log.info("Creating new dish: {}", dto.getName());

        if (dishRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Dish with name '" + dto.getName() + "' already exists");
        }

        DishType dishType = dishTypeRepository.findById(dto.getDishTypeId())
                .orElseThrow(() -> new RuntimeException("Dish type not found with ID: " + dto.getDishTypeId()));

        Dish dish = new Dish();
        dish.setName(dto.getName());
        dish.setPrice(dto.getPrice());
        dish.setDishType(dishType);
        dish.setDescription(dto.getDescription());
        dish.setImage(dto.getImage());
        dish.setEnergy(dto.getEnergy());
        dish.setProtein(dto.getProtein());
        dish.setFiber(dto.getFiber());
        dish.setCarbohydrates(dto.getCarbohydrates());
        dish.setFats(dto.getFats());
        dish.setIsAvailable(dto.getIsAvailable());

        Dish savedDish = dishRepository.save(dish);

        log.info("Dish created with ID: {}", savedDish.getId());
        return mapToResponseDto(savedDish);
    }

    public DishResponse updateDish(Long id, DishRequest dto) {
        log.info("Updating dish with ID: {}", id);

        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dish not found with ID: " + id));

        if (!dish.getName().equalsIgnoreCase(dto.getName()) &&
                dishRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new RuntimeException("Dish with name '" + dto.getName() + "' already exists");
        }

        DishType dishType = dishTypeRepository.findById(dto.getDishTypeId())
                .orElseThrow(() -> new RuntimeException("Dish type not found with ID: " + dto.getDishTypeId()));

        dish.setName(dto.getName());
        dish.setPrice(dto.getPrice());
        dish.setDishType(dishType);
        dish.setDescription(dto.getDescription());
        dish.setImage(dto.getImage());
        dish.setEnergy(dto.getEnergy());
        dish.setProtein(dto.getProtein());
        dish.setFiber(dto.getFiber());
        dish.setCarbohydrates(dto.getCarbohydrates());
        dish.setFats(dto.getFats());
        dish.setIsAvailable(dto.getIsAvailable());

        Dish updatedDish = dishRepository.save(dish);

        log.info("Dish updated with ID: {}", id);
        return mapToResponseDto(updatedDish);
    }

    public void deleteDish(Long id) {
        log.info("Deleting dish with ID: {}", id);

        if (!dishRepository.existsById(id)) {
            throw new RuntimeException("Dish not found with ID: " + id);
        }

        dishRepository.deleteById(id);
        log.info("Dish deleted with ID: {}", id);
    }

    private DishResponse mapToResponseDto(Dish dish) {
        return new DishResponse(
                dish.getId(),
                dish.getName(),
                dish.getPrice(),
                dish.getDishType() != null ? dish.getDishType().getId() : null,
                dish.getDishType() != null ? dish.getDishType().getType() : null,
                dish.getDescription(),
                dish.getImage(),
                dish.getEnergy(),
                dish.getProtein(),
                dish.getFiber(),
                dish.getCarbohydrates(),
                dish.getFats(),
                dish.getIsAvailable(),
                dish.getCreatedAt(),
                dish.getUpdatedAt()
        );
    }
}
