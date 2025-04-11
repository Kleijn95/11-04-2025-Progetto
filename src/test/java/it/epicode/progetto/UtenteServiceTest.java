package it.epicode.progetto;

import com.github.javafaker.Faker;
import it.epicode.progetto.utenti.Utente;
import it.epicode.progetto.utenti.UtenteRepository;
import it.epicode.progetto.utenti.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UtenteServiceTest {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Faker faker;

    @BeforeEach
    public void setUp() {
        utenteRepository.deleteAll();
    }

    @Test
    @DisplayName("Test creazione utente singolo")
    public void testSave() {
        Utente utente = new Utente();
        utente.setUsername("test");
        utente.setNomeCompleto("test nomecompleto");
        utente.setEmail("XXXXXXXXXXXXX");


        utenteRepository.save(utente);

        assertNotNull(utente.getId());

        Utente savedUtente = utenteRepository.findById(utente.getId()).orElse(null);
        assertNotNull(savedUtente);
        assertEquals("test" ,savedUtente.getUsername());
        assertEquals("test nomecompleto", savedUtente.getNomeCompleto());
        assertEquals("XXXXXXXXXXXXX", savedUtente.getEmail());


        System.out.println("Utente salvato con ID: " + utente.getId());

    }


    @Test
    @DisplayName("Test creazione utenti multipli con faker")
    public void testSaveAllFaker() {
        for (int i = 0; i < 10; i++) {
            Utente utente = new Utente();
            utente.setUsername(faker.name().username());
            utente.setNomeCompleto(faker.name().fullName());
            utente.setEmail(faker.internet().emailAddress());


            utenteRepository.save(utente);
        }


        long userCount = utenteRepository.count();
        assertEquals(10, userCount);


        Utente savedUtente = utenteRepository.findAll().get(0);
        assertNotNull(savedUtente);
        assertNotNull(savedUtente.getUsername());
        assertNotNull(savedUtente.getEmail());
    }


    @Test
    @DisplayName("Test username unico")
    @Transactional
    public void testUsernameUnico() {
        Utente utente1 = new Utente();
        utente1.setUsername("test");
        utente1.setNomeCompleto("test nomecompleto");
        utente1.setEmail("XXXXXXXXXXXXX");

        Utente utente2 = new Utente();
        utente2.setUsername("test");
        utente2.setNomeCompleto("test nomecompleto");
        utente2.setEmail("XXXXXXXXXXXXX");

        utenteRepository.save(utente1);

        try {
            utenteRepository.save(utente2);
        } catch (Exception e) {
            assertEquals("Username già esistente", e.getMessage());
        }
    }

}


