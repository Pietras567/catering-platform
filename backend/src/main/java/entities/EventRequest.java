package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "event_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "people_count", nullable = false)
    private Integer peopleCount;

    @Column(name = "event_date", nullable = false)
    private LocalDate date;

    @Column(name = "event_time", nullable = false)
    private LocalTime time;

    @Column(name = "menu_preferences", columnDefinition = "TEXT")
    private String preferences;

    @Column(name = "budget_per_person", precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(name = "dietary_preferences", columnDefinition = "TEXT")
    private String dietaryPreferences;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
