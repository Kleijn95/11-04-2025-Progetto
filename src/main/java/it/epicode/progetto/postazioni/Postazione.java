package it.epicode.progetto.postazioni;


import it.epicode.progetto.edifici.Edificio;
import it.epicode.progetto.prenotazioni.Prenotazione;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "postazioni")
public class Postazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String nome;
    private String descrizione;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPostazione tipo;
    @Column(nullable = false)

    private int numeroMassimoOccupanti;
    @ManyToOne
    private Edificio edificio;

    @OneToMany(mappedBy = "postazione", cascade = CascadeType.ALL)
    private List<Prenotazione> prenotazioni;

    @Override
    public String toString() {
        return "Postazione{" +
                "id=" + id +
                ", edificio=" + (edificio != null ? edificio.getCitta() : "N/A") +
                '}';
    }

}
