package entities;

import jakarta.persistence.*;

@Entity
public class ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;
}
