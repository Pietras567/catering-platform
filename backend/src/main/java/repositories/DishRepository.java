package repositories;

import entities.Dish;
import entities.DishType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByNameContainingIgnoreCase(String name);

    List<Dish> findByDishType(DishType dishType);

    List<Dish> findByDishTypeId(Long dishTypeId);

    List<Dish> findByIsAvailable(Boolean isAvailable);

    List<Dish> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT d FROM Dish d WHERE d.isAvailable = true ORDER BY d.name ASC")
    List<Dish> findAllAvailableDishes();

    @Query("SELECT d FROM Dish d WHERE d.isAvailable = true ORDER BY d.name ASC")
    Page<Dish> findAllAvailableDishesPaginated(Pageable pageable);

    @Query("SELECT d FROM Dish d WHERE d.energy BETWEEN :minEnergy AND :maxEnergy")
    List<Dish> findByEnergyRange(@Param("minEnergy") Integer minEnergy, @Param("maxEnergy") Integer maxEnergy);

    boolean existsByNameIgnoreCase(String name);
}
