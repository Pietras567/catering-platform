package events;

import dto.EventRequestDto;
import dto.EventRequestResponse;
import entities.EventRequest;
import entities.User;
import exceptions.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.EventRequestRepository;
import repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class EventRequestService {

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Autowired
    private UserRepository userRepository;

    public EventRequestResponse createEventRequest(EventRequestDto dto) {
        log.info("Creating a new event request: {}", dto.getEventType());

        User currentUser = getCurrentUser();

        EventRequest eventRequest = new EventRequest();
        eventRequest.setEventType(dto.getEventType());
        eventRequest.setPeopleCount(dto.getPeopleCount());
        eventRequest.setDate(dto.getDate());
        eventRequest.setTime(dto.getTime());
        eventRequest.setPreferences(dto.getPreferences());
        eventRequest.setBudget(dto.getBudget());
        eventRequest.setDietaryPreferences(dto.getDietaryPreferences());
        eventRequest.setClient(currentUser);

        EventRequest savedRequest = eventRequestRepository.save(eventRequest);

        log.info("The event request was created with the ID: {} for the client: {}",
                savedRequest.getId(), currentUser.getId());
        return mapToResponseDto(savedRequest);
    }

    @Transactional(readOnly = true)
    public EventRequestResponse getEventRequestById(Long id) {
        log.info("Fetching an event request with an ID: {}", id);

        EventRequest eventRequest = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event request not found: " + id));

        return mapToResponseDto(eventRequest);
    }

    @Transactional(readOnly = true)
    public EventRequestResponse getClientEventRequest(Long id) {
        log.info("The client fetching an event request with the ID: {}", id);

        User currentUser = getCurrentUser();
        EventRequest eventRequest = eventRequestRepository.findByIdAndClientId(id, currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException(id, currentUser.getId()));

        return mapToResponseDto(eventRequest);
    }

    @Transactional(readOnly = true)
    public List<EventRequestResponse> getClientEventRequests() {
        log.info("Retrieving client requests.");

        User currentUser = getCurrentUser();
        return eventRequestRepository.findByClientId(currentUser.getId())
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventRequestResponse> getAllEventRequests() {
        log.info("Fetching all event requests");

        return eventRequestRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<EventRequestResponse> getEventRequestsPaginated(Pageable pageable) {
        log.info("Fetching event requests with pagination");

        return eventRequestRepository.findAll(pageable)
                .map(this::mapToResponseDto);
    }

    @Transactional(readOnly = true)
    public List<EventRequestResponse> getUpcomingEvents() {
        log.info("Fetching upcoming events");

        return eventRequestRepository.findUpcomingEvents(LocalDate.now())
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventRequestResponse> searchByEventType(String eventType) {
        log.info("Search for events requests by type: {}", eventType);

        return eventRequestRepository.findByEventTypeContainingIgnoreCase(eventType)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public EventRequestResponse updateEventRequest(Long id, EventRequestDto dto) {
        log.info("Update event requests with ID: {}", id);

        EventRequest eventRequest = eventRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event request not found: " + id));

        eventRequest.setEventType(dto.getEventType());
        eventRequest.setPeopleCount(dto.getPeopleCount());
        eventRequest.setDate(dto.getDate());
        eventRequest.setTime(dto.getTime());
        eventRequest.setPreferences(dto.getPreferences());
        eventRequest.setBudget(dto.getBudget());
        eventRequest.setDietaryPreferences(dto.getDietaryPreferences());

        EventRequest updatedRequest = eventRequestRepository.save(eventRequest);

        log.info("The event request has been updated: {}", id);
        return mapToResponseDto(updatedRequest);
    }

    public EventRequestResponse updateClientEventRequest(Long id, EventRequestDto dto) {
        log.info("The client updates the event request with the ID: {}", id);

        User currentUser = getCurrentUser();
        EventRequest eventRequest = eventRequestRepository.findByIdAndClientId(id, currentUser.getId())
                .orElseThrow(() -> new AccessDeniedException(id, currentUser.getId()));

        eventRequest.setEventType(dto.getEventType());
        eventRequest.setPeopleCount(dto.getPeopleCount());
        eventRequest.setDate(dto.getDate());
        eventRequest.setTime(dto.getTime());
        eventRequest.setPreferences(dto.getPreferences());
        eventRequest.setBudget(dto.getBudget());
        eventRequest.setDietaryPreferences(dto.getDietaryPreferences());

        EventRequest updatedRequest = eventRequestRepository.save(eventRequest);

        log.info("The event request has been updated by the client: {}", id);
        return mapToResponseDto(updatedRequest);
    }

    public void deleteEventRequest(Long id) {
        log.info("Deleting the event request with ID: {}", id);

        if (!eventRequestRepository.existsById(id)) {
            throw new RuntimeException("Event request not found: " + id);
        }

        eventRequestRepository.deleteById(id);
        log.info("Event request has been removed: {}", id);
    }

    public void deleteClientEventRequest(Long id) {
        log.info("Client deletes event request with ID: {}", id);

        User currentUser = getCurrentUser();

        if (!eventRequestRepository.existsByIdAndClientId(id, currentUser.getId())) {
            throw new AccessDeniedException(id, currentUser.getId());
        }

        eventRequestRepository.deleteById(id);
        log.info("Event request was removed by client: {}", id);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    private EventRequestResponse mapToResponseDto(EventRequest eventRequest) {
        return new EventRequestResponse(
                eventRequest.getId(),
                eventRequest.getEventType(),
                eventRequest.getPeopleCount(),
                eventRequest.getDate(),
                eventRequest.getTime(),
                eventRequest.getPreferences(),
                eventRequest.getBudget(),
                eventRequest.getDietaryPreferences(),
                eventRequest.getCreatedAt(),
                eventRequest.getUpdatedAt()
        );
    }
}
