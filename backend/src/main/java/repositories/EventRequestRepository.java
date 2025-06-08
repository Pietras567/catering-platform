package repositories;

import entities.EventRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    List<EventRequest> findByEventTypeContainingIgnoreCase(String eventType);

    List<EventRequest> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<EventRequest> findByPeopleCountBetween(Integer minCount, Integer maxCount);

    List<EventRequest> findByClientId(Long clientId);

    Optional<EventRequest> findByIdAndClientId(Long id, Long clientId);

    @Query("SELECT e FROM EventRequest e WHERE e.date >= :fromDate ORDER BY e.date ASC")
    List<EventRequest> findUpcomingEvents(@Param("fromDate") LocalDate fromDate);

    @Query("SELECT COUNT(e) FROM EventRequest e WHERE e.date = :date")
    Long countEventsByDate(@Param("date") LocalDate date);

    boolean existsByIdAndClientId(Long id, Long clientId);
}

