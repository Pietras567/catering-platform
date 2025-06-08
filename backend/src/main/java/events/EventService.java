package events;

import dto.EventDishResponse;
import dto.EventDto;
import dto.EventResponse;
import dto.SelectedDish;
import entities.*;
import exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repositories.DishRepository;
import repositories.EventDishRepository;
import repositories.EventRepository;
import repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final EventDishRepository eventDishRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    @Transactional
    public EventResponse createEvent(EventDto eventDto) {
        log.info("Creating new event of type: {}", eventDto.getEventType());

        User currentUser = getCurrentUser();
        Event event = mapToEntity(eventDto, currentUser);
        Event savedEvent = eventRepository.save(event);

        createEventDishes(savedEvent, eventDto.getSelectedDishes());

        log.info("Event created successfully with ID: {}", savedEvent.getId());
        return mapToResponse(savedEvent);
    }

    public List<EventResponse> getClientEvents() {
        User currentUser = getCurrentUser();
        log.info("Fetching events for client: {}", currentUser.getUsername());

        List<Event> events = eventRepository.findByClientOrderByCreatedAtDesc(currentUser);
        return events.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public EventResponse getClientEvent(Long id) {
        User currentUser = getCurrentUser();
        log.info("Fetching event with ID: {} for client: {}", id, currentUser.getUsername());

        Event event = eventRepository.findByIdAndClient(id, currentUser).orElseThrow(() -> new ResourceNotFoundException("Event not found or access denied"));

        return mapToResponse(event);
    }

    @Transactional
    public EventResponse updateClientEvent(Long id, EventDto eventDto) {
        User currentUser = getCurrentUser();
        log.info("Updating event with ID: {} for client: {}", id, currentUser.getUsername());

        Event event = eventRepository.findByIdAndClient(id, currentUser).orElseThrow(() -> new ResourceNotFoundException("Event not found or access denied"));

        if (event.getStatus() != EventStatus.PENDING) {
            throw new IllegalStateException("Cannot modify event that is not in PENDING status");
        }

        updateEventFromDto(event, eventDto);
        updateEventDishes(event, eventDto.getSelectedDishes());

        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @Transactional
    public void deleteClientEvent(Long id) {
        User currentUser = getCurrentUser();
        log.info("Deleting event with ID: {} for client: {}", id, currentUser.getUsername());

        Event event = eventRepository.findByIdAndClient(id, currentUser).orElseThrow(() -> new ResourceNotFoundException("Event not found or access denied"));

        if (event.getStatus() != EventStatus.PENDING) {
            throw new IllegalStateException("Cannot delete event that is not in PENDING status");
        }

        eventRepository.delete(event);
    }

    public List<EventResponse> getAllEvents() {
        log.info("Fetching all events for manager");
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public Page<EventResponse> getEventsPaginated(Pageable pageable) {
        log.info("Fetching events with pagination");
        Page<Event> events = eventRepository.findAllByOrderByCreatedAtDesc(pageable);
        return events.map(this::mapToResponse);
    }

    public EventResponse getEventById(Long id) {
        log.info("Fetching event with ID: {}", id);
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return mapToResponse(event);
    }

    public List<EventResponse> getUpcomingEvents() {
        log.info("Fetching upcoming events");
        List<Event> events = eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
        return events.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<EventResponse> searchByEventType(String eventType) {
        log.info("Searching events by type: {}", eventType);
        List<Event> events = eventRepository.findByEventTypeContainingIgnoreCase(eventType);
        return events.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<EventResponse> getEventsByStatus(EventStatus status) {
        log.info("Fetching events by status: {}", status);
        List<Event> events = eventRepository.findByStatusOrderByCreatedAtDesc(status);
        return events.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public EventResponse updateEventStatus(Long id, EventStatus status) {
        log.info("Updating event status for ID: {} to: {}", id, status);
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setStatus(status);
        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);
        Event event = eventRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Event mapToEntity(EventDto dto, User client) {
        Event event = new Event();
        event.setEventType(dto.getEventType());
        event.setEventDate(dto.getDate());
        event.setEventTime(dto.getTime());
        event.setPeopleCount(dto.getPeopleCount());
        event.setTotalCost(dto.getTotalCost());
        event.setClient(client);
        event.setStatus(EventStatus.PENDING);
        return event;
    }

    private void updateEventFromDto(Event event, EventDto dto) {
        event.setEventType(dto.getEventType());
        event.setEventDate(dto.getDate());
        event.setEventTime(dto.getTime());
        event.setPeopleCount(dto.getPeopleCount());
        event.setTotalCost(dto.getTotalCost());
    }

    private void createEventDishes(Event event, List<SelectedDish> selectedDishes) {
        for (SelectedDish selectedDish : selectedDishes) {
            Dish dish = dishRepository.findById(selectedDish.getId()).orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + selectedDish.getId()));

            EventDish eventDish = new EventDish();
            eventDish.setEvent(event);
            eventDish.setDish(dish);
            eventDish.setQuantity(selectedDish.getQuantity());
            eventDish.setUnitPrice(selectedDish.getPrice());
            eventDish.setTotalPrice(selectedDish.getPrice().multiply(BigDecimal.valueOf(selectedDish.getQuantity())));

            eventDishRepository.save(eventDish);
        }
    }

    private void updateEventDishes(Event event, List<SelectedDish> selectedDishes) {
        eventDishRepository.deleteAll(event.getEventDishes());
        event.getEventDishes().clear();

        createEventDishes(event, selectedDishes);
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setEventType(event.getEventType());
        response.setDate(event.getEventDate());
        response.setTime(event.getEventTime());
        response.setPeopleCount(event.getPeopleCount());
        response.setTotalCost(event.getTotalCost());
        response.setStatus(event.getStatus());
        response.setClientUsername(event.getClient().getUsername());
        response.setClientEmail(event.getClient().getEmail());
        response.setCreatedAt(event.getCreatedAt());
        response.setUpdatedAt(event.getUpdatedAt());

        List<EventDishResponse> dishes = event.getEventDishes().stream().map(this::mapEventDishToResponse).collect(Collectors.toList());
        response.setDishes(dishes);

        return response;
    }

    private EventDishResponse mapEventDishToResponse(EventDish eventDish) {
        EventDishResponse response = new EventDishResponse();
        response.setDishId(eventDish.getDish().getId());
        response.setDishName(eventDish.getDish().getName());
        response.setDishSubtitle(eventDish.getDish().getDescription());
        response.setDishPrice(eventDish.getUnitPrice());
        response.setDishImage(eventDish.getDish().getImage());
        response.setDishCategory(eventDish.getDish().getDishType().getType());
        response.setQuantity(eventDish.getQuantity());
        response.setTotalPrice(eventDish.getTotalPrice());
        return response;
    }
}
