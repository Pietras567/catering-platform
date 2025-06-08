package repositories;

import entities.DishType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishTypeRepository extends JpaRepository<DishType, Long> {
    Optional<DishType> findByTypeIgnoreCase(String type);

    boolean existsByTypeIgnoreCase(String type);
}
