package it.epicode.progetto.edifici;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdificioService {

    @Autowired
    private EdificioRepository edificioRepository;

    @Autowired
    private Faker faker;

    public void creaEdifici(int count) {
        for (int i = 0; i < count; i++) {
            Edificio edificio = new Edificio();
            edificio.setNome(faker.company().name());
            edificio.setIndirizzo(faker.address().streetAddress());
            edificio.setCitta(faker.address().city());
            edificioRepository.save(edificio);
        }
    }

    public List<Edificio> trovaEdificiPerNome(String nome) {
        return edificioRepository.findByNome(nome);
    }
}
