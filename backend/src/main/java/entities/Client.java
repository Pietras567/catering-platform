package entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {
    @ManyToOne
    private ClientType clientType;

}
