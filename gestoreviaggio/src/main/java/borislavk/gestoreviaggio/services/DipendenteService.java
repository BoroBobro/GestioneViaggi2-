package borislavk.gestoreviaggio.services;

import borislavk.gestoreviaggio.entities.Dipendente;
import borislavk.gestoreviaggio.exeptions.BadRequestException;
import borislavk.gestoreviaggio.exeptions.NotFoundException;
import borislavk.gestoreviaggio.payloads.NewDipendenteDTO;
import borislavk.gestoreviaggio.repositories.DipendenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    public Dipendente save(NewDipendenteDTO payload) {
        this.dipendenteRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("L'email" + dipendente.getEmail() + "è già in uso!");
        });
        Dipendente newDipendente = new Dipendente(payload.name(), payload.surname(), payload.email(), payload.password());
        Dipendente savedDipendente = this.dipendenteRepository.save(newDipendente);
        log.info("L'utente con id: " + savedDipendente.getId() + "è stato salvato correttamente!");
        return savedDipendente;
    }

    public Page<Dipendente> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
        return this.dipendenteRepository.findAll(pageable);
    }

    public Dipendente findById(UUID userId) {
        return this.dipendenteRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public Dipendente findByIdAndUpdate(UUID userId, NewDipendenteDTO payload) {
        // 1. Cerco l'utente nel db
        Dipendente found = this.findById(userId);
        if (!found.getEmail().equals(payload.email())) // Il controllo dell'email lo faccio solo quando effettivamente mi sta passando una nuova email
            this.dipendenteRepository.findByEmail(payload.email()).ifPresent(user -> {
                throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
            });
        // 3. Modifico l'utente trovato nel db
        found.setNome(payload.name());
        found.setCognome(payload.surname());
        found.setEmail(payload.email());
        found.setPassword(payload.password());

        // 4. Salvo
        Dipendente modifiedDipendente = this.dipendenteRepository.save(found);

        // 5. Log
        log.info("L'utente con id " + found.getId() + " è stato modificato!");

        // 6. Return dell'utente modificato
        return modifiedDipendente;
    }

    public void findByIdAndDelete(UUID userId) {
        Dipendente found = this.findById(userId);
        this.dipendenteRepository.delete(found);
    }

    public Dipendente findByEmail(String email) {
        return this.dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }
}
