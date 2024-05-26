package ticket;

import client.Client;
import jakarta.persistence.*;
import lombok.Data;
import planet.Planet;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_planet_id", nullable = false)
    private Planet fromPlanet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_planet_id", nullable = false)
    private Planet toPlanet;


    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", client=" + client +
                ", fromPlanet=" + (fromPlanet != null ? fromPlanet.getName() : null) +
                ", toPlanet=" + (toPlanet != null ? toPlanet.getName() : null) +
                '}';
    }
}




