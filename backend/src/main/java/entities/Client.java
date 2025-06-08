package entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User{
    @ManyToOne
    private ClientType clientType;

}
