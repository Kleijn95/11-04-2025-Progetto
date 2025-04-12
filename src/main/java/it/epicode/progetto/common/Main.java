package it.epicode.progetto.common;

import it.epicode.progetto.edifici.EdificioService;
import it.epicode.progetto.postazioni.Postazione;
import it.epicode.progetto.postazioni.PostazioneRepository;
import it.epicode.progetto.postazioni.PostazioneService;
import it.epicode.progetto.postazioni.TipoPostazione;
import it.epicode.progetto.prenotazioni.Prenotazione;
import it.epicode.progetto.prenotazioni.PrenotazioneService;
import it.epicode.progetto.utenti.Utente;
import it.epicode.progetto.utenti.UtenteRepository;
import it.epicode.progetto.utenti.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
@Order(2)
public class Main implements CommandLineRunner {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PostazioneService postazioneService;

    @Autowired
    private PostazioneRepository  postazioneRepository;

    @Autowired
    private EdificioService edificioService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private UtenteRepository utenteRepository;

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Avvio del programma principale...");
        System.out.println("Benvenuti nel programma di gestione prenotazioni!");

        while (true) {
            System.out.println("\nSei già un utente registrato?");
            System.out.println("1. Sì");
            System.out.println("2. No");
            System.out.println("0. Esci");
            System.out.print("Inserisci il numero corrispondente all'opzione desiderata: ");

            int scelta = Integer.parseInt(scanner.nextLine());

            if (scelta == 0) {
                System.out.println("👋 Arrivederci!");
                break;
            } else if (scelta == 1) {
                System.out.print("Inserisci il tuo username: ");
                String username = scanner.nextLine();

                if (utenteService.utenteEsiste(username)) {
                    System.out.println("✅ Bentornato, " + username + "!");
                    mostraMenuUtente(username);
                } else {
                    System.out.println("⚠️ Utente non trovato.");
                    gestisciRegistrazione();
                }
            } else if (scelta == 2) {
                gestisciRegistrazione();
            } else {
                System.out.println("❌ Scelta non valida. Riprova.");
            }
        }
    }

    private void gestisciRegistrazione() {
        System.out.println("✨ Vuoi registrarti?");
        System.out.println("1. Sì");
        System.out.println("2. No");
        System.out.print("Scegli un'opzione: ");
        int sceltaRegistrazione = Integer.parseInt(scanner.nextLine());

        if (sceltaRegistrazione == 1) {
            System.out.print("Inserisci un nuovo username: ");
            String username = scanner.nextLine();

            if (utenteService.utenteEsiste(username)) {
                System.out.println("⚠️ Username già esistente. Prova con uno diverso.");
            } else {
                System.out.print("Inserisci nome completo: ");
                String nomeCompleto = scanner.nextLine();
                System.out.print("Inserisci email: ");
                String email = scanner.nextLine();

                Utente nuovoUtente = new Utente();
                nuovoUtente.setUsername(username);
                nuovoUtente.setNomeCompleto(nomeCompleto);
                nuovoUtente.setEmail(email);

                utenteRepository.save(nuovoUtente);

                System.out.println("🎉 Registrazione completata! Benvenuto, " + username + "!");
                mostraMenuUtente(username);
            }
        } else {
            System.out.println("👋 Arrivederci!");
        }
    }

    private void mostraMenuUtente(String username) {
        while (true) {
            System.out.println("\nCosa desideri fare, " + username + "?");
            System.out.println("1. Effettua una prenotazione");
            System.out.println("2. Visualizza le mie prenotazioni");
            System.out.println("3. Visualizza le postazioni disponibili per tipo");
            System.out.println("4. Visualizza le postazioni disponibili per citta");
            System.out.println("0. Torna al menu iniziale");
            System.out.print("Scegli un'opzione: ");

            int scelta = Integer.parseInt(scanner.nextLine());

            switch (scelta) {
                case 1:

                    System.out.println("📅 Funzionalità prenotazione.");
                    prenotazioneService.creaPrenotazioneDaConsole(username);
                    break;
                case 2:

                    System.out.println("📋 Visualizzazione prenotazioni.");


                    Optional<Utente> utenteOptional = utenteRepository.findByUsernameWithPrenotazioni(username);


                    if (utenteOptional.isPresent()) {
                        Utente utente = utenteOptional.get();


                        if (utente.getPrenotazioni().isEmpty()) {
                            System.out.println("🔍 Non hai prenotazioni.");
                        } else {
                            System.out.println("📋 Le tue prenotazioni:");
                            utente.getPrenotazioni().forEach(p -> System.out.println(p.toString()));  // Visualizza le prenotazioni
                        }
                    } else {
                        System.out.println("⚠️ Utente non trovato.");
                    }
                    break;





                case 3:
                    // mostra postazioni per tipo
                    System.out.println("🪑 Visualizzazione postazioni disponibili per tipo.");
                    System.out.println("Che tipo di postazione cerchi?");
                    System.out.println("1. Privato");
                    System.out.println("2. OpenSpace");
                    System.out.println("3. Sala di riunione");
                    System.out.print("Scegli un'opzione: ");
                    int sceltaTipo = Integer.parseInt(scanner.nextLine());
                    TipoPostazione tipoPostazione = null;
                    switch (sceltaTipo) {
                        case 1:
                            tipoPostazione = TipoPostazione.PRIVATO;
                            break;
                        case 2:
                            tipoPostazione = TipoPostazione.OPENSPACE;
                            break;
                        case 3:
                            tipoPostazione = TipoPostazione.SALA_RIUNIONI;
                            break;
                        default:
                            System.out.println("❌ Scelta non valida. Riprova.");
                            break;
                    }
                    if (tipoPostazione != null) {

                        List<Postazione> postazioni = postazioneRepository.findByTipo(tipoPostazione);
                        if (postazioni.isEmpty()) {
                            System.out.println("🚫 Nessuna postazione disponibile per questo tipo.");
                        } else {

                            System.out.println("Postazioni disponibili per il tipo " + tipoPostazione + ":");
                            for (Postazione p : postazioni) {
                                System.out.println(p);
                            }
                        }
                    }
                case 4:

                    System.out.println("🪑 Visualizzazione postazioni disponibili per città.");
                    System.out.print("Inserisci il nome della città: ");
                    String citta = scanner.nextLine();


                    List<Postazione> postazioni = postazioneRepository.findByEdificioCitta(citta);

                    if (postazioni.isEmpty()) {
                        System.out.println("🚫 Nessuna postazione disponibile in questa città.");
                    } else {

                        System.out.println("Postazioni disponibili nella città " + citta + ":");
                        for (Postazione p : postazioni) {
                            System.out.println(p);
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("❌ Scelta non valida. Riprova.");
            }
        }
    }
}
