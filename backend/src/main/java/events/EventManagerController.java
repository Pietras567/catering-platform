package events;

import dto.EventResponse;
import entities.EventStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
@Slf4j
public class EventManagerController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        log.info("Manager fetching all events");
        List<EventResponse> response = eventService.getAllEvents();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<EventResponse>> getEventsPaginated(Pageable pageable) {
        log.info("Manager fetching events with pagination");
        Page<EventResponse> response = eventService.getEventsPaginated(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        log.info("Manager fetching event with ID: {}", id);
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        log.info("Manager fetching upcoming events");
        List<EventResponse> response = eventService.getUpcomingEvents();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponse>> searchByEventType(@RequestParam String eventType) {
        log.info("Manager searching events by type: {}", eventType);
        List<EventResponse> response = eventService.searchByEventType(eventType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventResponse>> getEventsByStatus(@PathVariable EventStatus status) {
        log.info("Manager fetching events by status: {}", status);
        List<EventResponse> response = eventService.getEventsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EventResponse> updateEventStatus(@PathVariable Long id, @RequestParam EventStatus status) {
        log.info("Manager updating event status for ID: {} to: {}", id, status);
        EventResponse response = eventService.updateEventStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.info("Manager deleting event with ID: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
