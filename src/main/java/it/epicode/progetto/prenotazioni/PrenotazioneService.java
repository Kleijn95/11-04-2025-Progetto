package it.epicode.progetto.prenotazioni;

import com.github.javafaker.Faker;
import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.postazioni.PostazioneRepository;
import it.epicode.progetto.utenti.Utente;
import it.epicode.progetto.utenti.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

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
            //System.out.println("Prenotazione creata per l'utente " + prenotazione.getUtente().getId() + " alla postazione " + prenotazione.getPostazione().getId());
        }
    }


    public boolean creaPrenotazione(Utente utente, Long idPostazione, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {

        List<Prenotazione> prenotazioniUtente = prenotazioneRepository.findByUtenteAndData(utente, data);
        if (!prenotazioniUtente.isEmpty()) {
            System.out.println("⚠️ Hai già una prenotazione per questa data.");
            return false;
        }


        Postazione postazione = postazioneRepository.findById(idPostazione).orElse(null);
        if (postazione == null) {
            System.out.println("❌ Postazione non trovata.");
            return false;
        }


        boolean occupata = prenotazioneRepository.existsByPostazioneAndDataAndOraInizioBetweenOrOraFineBetween(
                postazione, data, oraInizio, oraInizio.plusMinutes(1), oraFine, oraFine.plusMinutes(1)
        );
        if (occupata) {
            System.out.println("⛔ La postazione è già occupata in questo orario.");
            return false;
        }

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setPostazione(postazione);
        prenotazione.setData(data);
        prenotazione.setOraInizio(oraInizio);
        prenotazione.setOraFine(oraFine);

        prenotazioneRepository.save(prenotazione);
        System.out.println("✅ Prenotazione creata con successo!");
        return true;
    }

    public void creaPrenotazioneDaConsole(String username) {
        Utente utente = utenteRepository.findByUsername(username);
        if (utente == null) {
            System.out.println("❌ Utente non trovato.");
            return;
        }

        List<Postazione> postazioni = postazioneRepository.findAll();
        if (postazioni.isEmpty()) {
            System.out.println("⚠️ Nessuna postazione disponibile.");
            return;
        }

        System.out.println("📋 Ecco le postazioni disponibili:");
        postazioni.forEach(p -> {
            System.out.println("ID: " + p.getId() + " | Tipo: " + p.getTipo() + " | Edificio: " + p.getEdificio().getNome() + " - " + p.getEdificio().getCitta());
        });


        Scanner scanner = new Scanner(System.in);

        System.out.print("🪑 Inserisci l'ID della postazione desiderata: ");
        Long idPostazione = Long.parseLong(scanner.nextLine());

        System.out.print("📅 Inserisci la data (formato YYYY-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());

        System.out.print("⏰ Inserisci ora di inizio (formato HH:mm): ");
        LocalTime oraInizio = LocalTime.parse(scanner.nextLine());

        System.out.print("⏰ Inserisci ora di fine (formato HH:mm): ");
        LocalTime oraFine = LocalTime.parse(scanner.nextLine());

        boolean success = creaPrenotazione(utente, idPostazione, data, oraInizio, oraFine);
        if (success) {
            System.out.println("✅ Prenotazione completata!");
        }
    }


    public List<Prenotazione> trovaPrenotazioniPerUtenteId(Long utenteId) {
        return prenotazioneRepository.findByUtenteId(utenteId);






}}
