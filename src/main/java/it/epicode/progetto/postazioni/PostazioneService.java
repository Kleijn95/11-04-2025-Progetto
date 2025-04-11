package it.epicode.progetto.postazioni;

import com.github.javafaker.Faker;
import it.epicode.progetto.edifici.Edificio;
import it.epicode.progetto.edifici.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostazioneService {

    @Autowired
    private PostazioneRepository postazioneRepository;

    @Autowired
    private EdificioRepository edificioRepository;

    @Autowired
    private Faker faker;

    public void creaPostazioni(int count) {
        for (int i = 0; i < count; i++) {
            Postazione postazione = new Postazione();
            postazione.setNome(faker.company().name());
            postazione.setDescrizione(faker.lorem().sentence());
            postazione.setTipo(TipoPostazione.values()[faker.random().nextInt(TipoPostazione.values().length)]);
            postazione.setNumeroMassimoOccupanti(faker.random().nextInt(1, 10));


            List<Edificio> edifici = edificioRepository.findAll();
            if (edifici != null && !edifici.isEmpty()) {
                int index = faker.random().nextInt(0, edifici.size());
                if (index < edifici.size()) {
                    Edificio edificio = edifici.get(index);
                    postazione.setEdificio(edificio);
                } else {

                    System.out.println("Indice non valido per la lista degli edifici.");
                }
            } else {

                System.out.println("Nessun edificio disponibile.");
            }

            postazioneRepository.save(postazione);
        }
    }


    public List<Postazione> trovaPostazioniPerTipoETipoEdificio(TipoPostazione tipo, String citta) {
        return postazioneRepository.findByTipoAndCitta(tipo, citta);
    }
}
