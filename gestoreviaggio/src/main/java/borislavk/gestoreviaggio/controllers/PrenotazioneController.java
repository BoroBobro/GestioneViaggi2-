package borislavk.gestoreviaggio.controllers;


import borislavk.gestoreviaggio.entities.Dipendente;
import borislavk.gestoreviaggio.entities.Prenotazione;
import borislavk.gestoreviaggio.entities.Viaggio;
import borislavk.gestoreviaggio.repositories.DipendenteRepository;
import borislavk.gestoreviaggio.repositories.PrenotazioneRepository;
import borislavk.gestoreviaggio.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {
    @Autowired
    private PrenotazioneRepository prenotazioneRepo;

    @Autowired
    private DipendenteRepository dipendenteRepo;

    @Autowired
    private ViaggioRepository viaggioRepo;

    @PostMapping
    public ResponseEntity<?> creaPrenotazione(@RequestParam UUID dipendenteId,
                                              @RequestParam UUID viaggioId,
                                              @RequestBody @Validated Prenotazione prenotazione) {

        Optional<Dipendente> dipOpt = dipendenteRepo.findById(dipendenteId);
        Optional<Viaggio> viagOpt = viaggioRepo.findById(viaggioId);

        if (dipOpt.isEmpty() || viagOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Dipendente o Viaggio non esistente");
        }

        Dipendente dipendente = dipOpt.get();

        boolean giàPrenotato = prenotazioneRepo
                .findByDipendenteAndDataRichiesta(dipendente, prenotazione.getDataRichiesta())
                .isPresent();

        if (giàPrenotato) {
            return ResponseEntity.status(409).body("Prenotazione già esistente per questo dipendente in questa data");
        }

        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viagOpt.get());

        return ResponseEntity.ok(prenotazioneRepo.save(prenotazione));
    }

    @GetMapping
    public List<Prenotazione> getAll() {
        return prenotazioneRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prenotazione> getById(@PathVariable UUID id) {
        return prenotazioneRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        prenotazioneRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
