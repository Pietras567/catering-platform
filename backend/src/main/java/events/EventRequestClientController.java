package events;

import dto.EventRequestDto;
import dto.EventRequestResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/client/event-requests")
public class EventRequestClientController {

    @Autowired
    private EventRequestService eventRequestService;

    @PostMapping
    public ResponseEntity<EventRequestResponse> createEventRequest(@Valid @RequestBody EventRequestDto eventRequestDto) {

        log.info("Client creating new event request: {}", eventRequestDto.getEventType());

        EventRequestResponse response = eventRequestService.createEventRequest(eventRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventRequestResponse>> getClientEventRequests() {
        log.info("Client fetching his/her all event requests.");

        List<EventRequestResponse> response = eventRequestService.getClientEventRequests();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventRequestResponse> getClientEventRequest(@PathVariable Long id) {
        log.info("Client fetching his/her event request with ID: {}", id);

        EventRequestResponse response = eventRequestService.getClientEventRequest(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventRequestResponse> updateClientEventRequest(@PathVariable Long id, @Valid @RequestBody EventRequestDto eventRequestDto) {

        log.info("Client updates event request with ID: {}", id);

        EventRequestResponse response = eventRequestService.updateClientEventRequest(id, eventRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientEventRequest(@PathVariable Long id) {
        log.info("Client removes event request with ID: {}", id);

        eventRequestService.deleteClientEventRequest(id);
        return ResponseEntity.noContent().build();
    }
}
