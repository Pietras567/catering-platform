package repositories;

import entities.Event;
import entities.EventStatus;
import entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByClientOrderByCreatedAtDesc(User client);

    Optional<Event> findByIdAndClient(Long id, User client);

    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate date);

    List<Event> findByEventTypeContainingIgnoreCase(String eventType);

    List<Event> findByStatusOrderByCreatedAtDesc(EventStatus status);

    Page<Event> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate ORDER BY e.eventDate ASC")
    List<Event> findEventsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Event e WHERE e.client = :client AND e.status = :status ORDER BY e.createdAt DESC")
    List<Event> findByClientAndStatus(@Param("client") User client, @Param("status") EventStatus status);
}
