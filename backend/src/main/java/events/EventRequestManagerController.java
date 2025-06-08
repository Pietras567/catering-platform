package events;

import dto.EventRequestDto;
import dto.EventRequestResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin/event-requests")
public class EventRequestManagerController {

    @Autowired
    private EventRequestService eventRequestService;

    @GetMapping
    public ResponseEntity<List<EventRequestResponse>> getAllEventRequests() {
        log.info("Manager fetching all event requests.");

        List<EventRequestResponse> response = eventRequestService.getAllEventRequests();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<EventRequestResponse>> getEventRequestsPaginated(Pageable pageable) {
        log.info("Manager fetching event requests with pagination:");

        Page<EventRequestResponse> response = eventRequestService.getEventRequestsPaginated(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRequestResponse> getEventRequest(@PathVariable Long id) {
        log.info("Manager fetching event request with ID: {}", id);

        EventRequestResponse response = eventRequestService.getEventRequestById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventRequestResponse>> getUpcomingEvents() {
        log.info("Manager fetching upcoming event requests.");

        List<EventRequestResponse> response = eventRequestService.getUpcomingEvents();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventRequestResponse>> searchEventsByType(@RequestParam String eventType) {

        log.info("Manager searches event requests by type {}", eventType);

        List<EventRequestResponse> response = eventRequestService.searchByEventType(eventType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventRequestResponse> updateEventRequestAsManager(@PathVariable Long id, @Valid @RequestBody EventRequestDto eventRequestDto) {

        log.info("Manager updates event request with ID: {}", id);

        EventRequestResponse response = eventRequestService.updateEventRequest(id, eventRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventRequestAsManager(@PathVariable Long id) {
        log.info("Manager removes event request with ID: {}", id);

        eventRequestService.deleteEventRequest(id);
        return ResponseEntity.noContent().build();
    }
}
