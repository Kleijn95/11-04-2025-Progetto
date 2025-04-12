package it.epicode.progetto.utenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Utente findByUsername(String username);


    @Query("SELECT u FROM Utente u WHERE(SELECT COUNT(p) FROM Prenotazione p WHERE p.utente = u) < 2")
    List<Utente> findUtentiConMenoDiDuePrenotazioni();


    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.prenotazioni WHERE u.id = :id")
    Optional<Utente> findByIdWithPrenotazioni(@Param("id") Long id);

    @Query("SELECT u FROM Utente u LEFT JOIN FETCH u.prenotazioni WHERE u.username = :username")
    Optional<Utente> findByUsernameWithPrenotazioni(@Param("username") String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
