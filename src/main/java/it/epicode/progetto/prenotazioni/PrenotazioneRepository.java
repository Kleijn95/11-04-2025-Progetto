package it.epicode.progetto.prenotazioni;

import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.utenti.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {


    List<Prenotazione> findByUtenteAndData(Utente utente, LocalDate data);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Prenotazione p " +
            "WHERE p.postazione = :postazione " +
            "AND p.data = :data " +
            "AND (p.oraInizio BETWEEN :oraInizioStart AND :oraInizioEnd " +
            "OR p.oraFine BETWEEN :oraFineStart AND :oraFineEnd)")
    boolean existsByPostazioneAndDataAndOraInizioBetweenOrOraFineBetween(
            @Param("postazione") Postazione postazione,
            @Param("data") LocalDate data,
            @Param("oraInizioStart") LocalTime oraInizioStart,
            @Param("oraInizioEnd") LocalTime oraInizioEnd,
            @Param("oraFineStart") LocalTime oraFineStart,
            @Param("oraFineEnd") LocalTime oraFineEnd
    );
}
