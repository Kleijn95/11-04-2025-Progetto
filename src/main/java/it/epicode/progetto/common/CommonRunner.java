package it.epicode.progetto.common;

import it.epicode.progetto.edifici.EdificioService;
import it.epicode.progetto.postazioni.PostazioneService;
import it.epicode.progetto.prenotazioni.PrenotazioneService;
import it.epicode.progetto.utenti.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommonRunner implements CommandLineRunner {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PostazioneService postazioneService;

    @Autowired
    private EdificioService edificioService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Override
    public void run(String... args) throws Exception {
        // Crea 10 utenti
        utenteService.creaUtenti(10);
        System.out.println("Creati 10 utenti con successo!");

        // Crea 5 edifici
        edificioService.creaEdifici(10);

        System.out.println("Creati 10 edifici con successo!");

        // Crea 10 postazioni
        postazioneService.creaPostazioni(10);

        System.out.println("Create 10 postazioni con successo!");

        // Crea 10 prenotazioni
        prenotazioneService.creaPrenotazioni(10);

        System.out.println("Create 10 prenotazioni con successo!");

        System.out.println("Dati di test creati con successo!");
    }
}
