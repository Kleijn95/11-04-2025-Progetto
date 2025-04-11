package it.epicode.progetto.utenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByUsername(String username);

    // query per cercare utente con meno di 2 prenotazioni
    @Query("SELECT u FROM Utente u WHERE(SELECT COUNT(p) FROM Prenotazione p WHERE p.utente = u) < 2")
    List<Utente> findUtentiConMenoDiDuePrenotazioni();


    boolean existsByUsername(String username);



}
