package it.epicode.progetto;

import com.github.javafaker.Faker;
import it.epicode.progetto.edifici.EdificioRepository;
import it.epicode.progetto.edifici.EdificioService;
import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.postazioni.PostazioneRepository;
import it.epicode.progetto.postazioni.PostazioneService;
import it.epicode.progetto.postazioni.TipoPostazione;
import it.epicode.progetto.prenotazioni.Prenotazione;
import it.epicode.progetto.prenotazioni.PrenotazioneRepository;
import it.epicode.progetto.prenotazioni.PrenotazioneService;
import it.epicode.progetto.utenti.Utente;
import it.epicode.progetto.utenti.UtenteRepository;
import it.epicode.progetto.utenti.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PrenotazioneServiceTest {

    @Autowired
    private PrenotazioneService prenotazioneService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PostazioneService postazioneService;
    @Autowired
    private EdificioService edificioService;
    @Autowired
    private Faker faker;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private PostazioneRepository postazioneRepository;
    @Autowired
    private EdificioRepository edificioRepository;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @BeforeEach
    public void setUp() {
        prenotazioneRepository.deleteAll();
        postazioneRepository.deleteAll();
        edificioRepository.deleteAll();
        utenteRepository.deleteAll();
    }

    @Test
    @DisplayName("Test creazione di una prenotazione valida")
    public void testCreazionePrenotazioneValida() {
        // Crezione utente
        Utente utente = new Utente();
        utente.setUsername("utente1");
        utente.setNomeCompleto("Nome Utente");
        utente.setEmail("email@dominio.com");
        utenteRepository.save(utente);

        // Crezione postazione
        Postazione postazione = new Postazione();
        postazione.setNome("Postazione 1");
        postazione.setTipo(TipoPostazione.OPENSPACE);
        postazione.setDescrizione("Postazione per lavoro");
        postazioneRepository.save(postazione);

        // Crezione prenotiazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setPostazione(postazione);
        prenotazione.setData(LocalDate.of(2025, 4, 12));
        prenotazione.setOraInizio(LocalTime.of(9,0));  // Impostiamo l'ora di inizio
        prenotazione.setOraFine(LocalTime.of(17,0));

        prenotazioneRepository.save(prenotazione);

        // Verifico che sia stata salvata correttamente
        assertNotNull(prenotazione.getId());
        Prenotazione savedPrenotazione = prenotazioneRepository.findById(prenotazione.getId()).orElse(null);
        assertNotNull(savedPrenotazione);
        assertNotNull(savedPrenotazione.getUtente());
        assertNotNull(savedPrenotazione.getPostazione());
        assertEquals(utente.getUsername(), savedPrenotazione.getUtente().getUsername());
        assertEquals(postazione.getNome(), savedPrenotazione.getPostazione().getNome());
        assertEquals(LocalDate.of(2025, 4, 12), savedPrenotazione.getData());

}


    @Test
    public void testPrenotazionePostazioneOccupata() {
        // Creazione postazione
        Postazione postazione = new Postazione();
        postazione.setNome("Postazione 1");
        postazione.setTipo(TipoPostazione.OPENSPACE);
        postazione.setDescrizione("Postazione per lavoro");
        postazioneRepository.save(postazione);

        // Creazione utente
        Utente utente1 = new Utente();
        utente1.setUsername("utente1");
        utente1.setNomeCompleto("Nome Utente");
        utente1.setEmail("XXXXXXXXXXXXXXXXX");
        utenteRepository.save(utente1);

        // Crezione prima prenotazione
        Prenotazione prenotazione1 = new Prenotazione();
        prenotazione1.setPostazione(postazione);
        prenotazione1.setUtente(utente1);
        prenotazione1.setData(LocalDate.now());
        prenotazione1.setOraInizio(LocalTime.of(10, 0));
        prenotazione1.setOraFine(LocalTime.of(12, 0));
        prenotazioneRepository.save(prenotazione1);

        // Tento di creare una seconda prenotazione per la stessa postazione nello stesso orario
        Prenotazione prenotazione2 = new Prenotazione();
        prenotazione2.setPostazione(postazione);
        prenotazione2.setUtente(new Utente());
        prenotazione2.getUtente().setUsername("utente2");
        prenotazione2.getUtente().setNomeCompleto("Nome Utente 2");
        prenotazione2.getUtente().setEmail("XXXXXXXXXXXXXXXXXX");
        utenteRepository.save(prenotazione2.getUtente());
        prenotazione2.setData(LocalDate.now());
        prenotazione2.setOraInizio(LocalTime.of(10, 0));
        prenotazione2.setOraFine(LocalTime.of(12, 0));


        boolean prenotazioneEsistente = prenotazioneRepository.existsByPostazioneAndDataAndOraInizioBetweenOrOraFineBetween(
                prenotazione2.getPostazione(),
                prenotazione2.getData(),
                prenotazione2.getOraInizio(),
                prenotazione2.getOraInizio().plusMinutes(1),  // per il range di oraInizio
                prenotazione2.getOraFine(),
                prenotazione2.getOraFine().plusMinutes(1)     // per il range di oraFine
        );


        assertTrue(prenotazioneEsistente, "La postazione dovrebbe essere occupata per questo orario.");
    }




    @Test
    public void testUtentePrenotazioniMultipleSuStessaData() {
        // Creazione di due postazioni
        Postazione postazione1 = new Postazione();
        postazione1.setNome("Postazione 1");
        postazione1.setTipo(TipoPostazione.OPENSPACE);
        postazione1.setDescrizione("Postazione per lavoro");
        postazioneRepository.save(postazione1);

        Postazione postazione2 = new Postazione();
        postazione2.setNome("Postazione 2");
        postazione2.setTipo(TipoPostazione.SALA_RIUNIONI);
        postazione2.setDescrizione("Postazione privata");
        postazioneRepository.save(postazione2);

        // Creazione di UN utente
        Utente utente1 = new Utente();
        utente1.setUsername("utente1");
        utente1.setNomeCompleto("Nome Utente");
        utente1.setEmail("utente1@example.com");
        utenteRepository.save(utente1);

        // Creazione prima prenotazione
        Prenotazione prenotazione1 = new Prenotazione();
        prenotazione1.setPostazione(postazione1);
        prenotazione1.setUtente(utente1);
        prenotazione1.setData(LocalDate.now());
        prenotazione1.setOraInizio(LocalTime.of(10, 0));
        prenotazione1.setOraFine(LocalTime.of(12, 0));
        prenotazioneRepository.save(prenotazione1);

        // Creazione della seconda prenotazione per lo stesso utente su una postazione diversa
        Prenotazione prenotazione2 = new Prenotazione();
        prenotazione2.setPostazione(postazione2);
        prenotazione2.setUtente(utente1);
        prenotazione2.setData(LocalDate.now());
        prenotazione2.setOraInizio(LocalTime.of(10, 0));
        prenotazione2.setOraFine(LocalTime.of(12, 0));

        // Trovo tutte le prenotazioni dell'utente per la data specifica
        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository.findByUtenteAndData(utente1, prenotazione2.getData());

        // Verifico se esiste già una prenotazione con orario che si sovrappone
        boolean orarioOccupato = prenotazioniEsistenti.stream().anyMatch(p ->
                (p.getOraInizio().isBefore(prenotazione2.getOraFine()) && p.getOraFine().isAfter(prenotazione2.getOraInizio()))
        );

        // Se l'orario è già occupato, l'utente non dovrebbe poter prenotare!
        assertTrue(orarioOccupato, "L'utente non può prenotare due postazioni nello stesso orario.");
    }




}
