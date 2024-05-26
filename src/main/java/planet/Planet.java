package planet;

import jakarta.persistence.*;
import lombok.Data;
import ticket.Ticket;

import java.util.List;

@Data
@Entity
@Table(name = "planet")
public class Planet {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "fromPlanet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> fromTickets;

    @OneToMany(mappedBy = "toPlanet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ticket> toTickets;

    @Override
    public String toString() {
        return "Planet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' + '}';
    }
}

