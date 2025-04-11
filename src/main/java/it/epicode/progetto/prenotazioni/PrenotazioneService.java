package it.epicode.progetto.prenotazioni;

import com.github.javafaker.Faker;
import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.postazioni.PostazioneRepository;
import it.epicode.progetto.utenti.Utente;
import it.epicode.progetto.utenti.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private PostazioneRepository postazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Faker faker;

    public void creaPrenotazioni(int count) {
        for (int i = 0; i < count; i++) {
            Prenotazione prenotazione = new Prenotazione();


            List<Utente> utenti = utenteRepository.findAll();
            if (!utenti.isEmpty()) {
                int indexUtente = faker.random().nextInt(utenti.size());
                Utente utente = utenti.get(indexUtente);
                prenotazione.setUtente(utente);
            } else {
                System.out.println("Nessun utente disponibile.");
                continue;
            }


            LocalDate data = LocalDate.now().plusDays(faker.random().nextInt(1, 30));
            prenotazione.setData(data);


            List<Prenotazione> prenotazioniUtente = prenotazioneRepository.findByUtenteAndData(prenotazione.getUtente(), prenotazione.getData());
            if (!prenotazioniUtente.isEmpty()) {
                System.out.println("L'utente ha già una prenotazione per questa data.");
                continue;
            }


            List<Postazione> postazioni = postazioneRepository.findAll();
            if (!postazioni.isEmpty()) {
                int indexPostazione = faker.random().nextInt(postazioni.size());
                Postazione postazione = postazioni.get(indexPostazione);
                prenotazione.setPostazione(postazione);
            } else {
                System.out.println("Nessuna postazione disponibile.");
                continue;
            }


            LocalTime oraInizio = LocalTime.of(faker.random().nextInt(8, 17), faker.random().nextInt(0, 59));
            LocalTime oraFine = oraInizio.plusHours(faker.random().nextInt(1, 3));
            prenotazione.setOraInizio(oraInizio);
            prenotazione.setOraFine(oraFine);

            boolean prenotazioneEsistente = prenotazioneRepository.existsByPostazioneAndDataAndOraInizioBetweenOrOraFineBetween(
                    prenotazione.getPostazione(),
                    prenotazione.getData(),
                    prenotazione.getOraInizio(),
                    prenotazione.getOraInizio().plusMinutes(1),
                    prenotazione.getOraFine(),
                    prenotazione.getOraFine().plusMinutes(1)
            );

            if (prenotazioneEsistente) {
                System.out.println("La postazione è già prenotata per questa data e ora.");
                continue;
            }


            prenotazioneRepository.save(prenotazione);
            System.out.println("Prenotazione creata per l'utente " + prenotazione.getUtente().getId() + " alla postazione " + prenotazione.getPostazione().getId());
        }
    }





    public List<Prenotazione> trovaPrenotazioniPerUtente(Utente utente) {
        return prenotazioneRepository.findByUtenteAndData(utente, LocalDate.now());
    }


}
