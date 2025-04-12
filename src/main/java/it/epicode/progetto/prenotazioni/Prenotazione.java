package it.epicode.progetto.prenotazioni;


import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.utenti.Utente;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private LocalDate data;
    @Column(nullable = false)
    private LocalTime oraInizio;
    @Column(nullable = false)
    private LocalTime oraFine;

    @ManyToOne
    private Utente utente;
    @ManyToOne
    private Postazione postazione;

    @Override

    public String toString() {
        return "Prenotazione{" +
                "id=" + id +
                ", data=" + data +
                ", oraInizio=" + oraInizio +
                ", oraFine=" + oraFine +
                ", postazione=" + (postazione != null ? "Id postazione= " + postazione.getId() + " - " + "Citta " + postazione.getEdificio().getCitta() : "N/A") +
                '}';
    }


}
