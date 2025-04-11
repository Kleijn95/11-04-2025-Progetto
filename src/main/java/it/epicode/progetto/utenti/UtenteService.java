package it.epicode.progetto.utenti;


import com.github.javafaker.Faker;
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
        for (int i = 0; i < count; i++) {
            Utente utente = new Utente();
            utente.setUsername(faker.name().username());
            utente.setNomeCompleto(faker.name().fullName());
            utente.setEmail(faker.internet().emailAddress());
            utenteRepository.save(utente);
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



    public boolean utenteEsiste(String username) {
        return utenteRepository.existsByUsername(username);
    }
}
