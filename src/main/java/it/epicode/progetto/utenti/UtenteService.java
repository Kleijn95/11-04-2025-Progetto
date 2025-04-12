package it.epicode.progetto.utenti;


import com.github.javafaker.Faker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Faker faker;



    public void creaUtenti(int count) {
        int created = 0;

        while (created < count) {
            String username = faker.name().username();
            String email = faker.internet().emailAddress();

            boolean usernameExists = utenteRepository.existsByUsername(username);
            boolean emailExists = utenteRepository.existsByEmail(email);

            if (!usernameExists && !emailExists) {
                Utente utente = new Utente();
                utente.setUsername(username);
                utente.setNomeCompleto(faker.name().fullName());
                utente.setEmail(email);
                utenteRepository.save(utente);
                created++;
            }
        }
    }


    public List<Utente> trovaUtentiConMenoDiDuePrenotazioni() {
        List<Utente> utenti = utenteRepository.findUtentiConMenoDiDuePrenotazioni();
        if (!utenti.isEmpty()) {  // Verifica che la lista non sia vuota
            return utenti;
        } else {
            System.out.println("Nessun utente con meno di due prenotazioni.");
            return List.of();
        }


    }
    public Utente trovaUtentePerUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public Utente getUtenteConPrenotazioni(Long id) {
        return utenteRepository.findByIdWithPrenotazioni(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));}

    public boolean utenteEsiste(String username) {
        return utenteRepository.existsByUsername(username);
    }
}

