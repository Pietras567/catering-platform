package events;

import dto.EventDto;
import dto.EventResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/events")
@Slf4j
public class EventClientController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventDto eventDto) {
        log.info("Client creating new event: {}", eventDto.getEventType());
        EventResponse response = eventService.createEvent(eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getClientEvents() {
        log.info("Client fetching their events");
        List<EventResponse> response = eventService.getClientEvents();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getClientEvent(@PathVariable Long id) {
        log.info("Client fetching event with ID: {}", id);
        EventResponse response = eventService.getClientEvent(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateClientEvent(@PathVariable Long id, @Valid @RequestBody EventDto eventDto) {
        log.info("Client updating event with ID: {}", id);
        EventResponse response = eventService.updateClientEvent(id, eventDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientEvent(@PathVariable Long id) {
        log.info("Client deleting event with ID: {}", id);
        eventService.deleteClientEvent(id);
        return ResponseEntity.noContent().build();
    }
}
