package repositories;

import entities.EventDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDishRepository extends JpaRepository<EventDish, Long> {
    List<EventDish> findByEventId(Long eventId);
}
