package borislavk.gestoreviaggio.controllers;


import borislavk.gestoreviaggio.entities.Viaggio;
import borislavk.gestoreviaggio.enums.StatoViggio;
import borislavk.gestoreviaggio.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {

    @Autowired
    private ViaggioRepository viaggioRepository;

    @PostMapping
    public ResponseEntity<Viaggio> creaViaggio(@RequestBody @Validated Viaggio viaggio) {
        return ResponseEntity.ok(viaggioRepository.save(viaggio));

    }

    @GetMapping
    public List<Viaggio> getAll() {
        return viaggioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaggio> getById(@PathVariable UUID id) {
        return viaggioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/stato")
    public ResponseEntity<Viaggio> aggiornaStato(@PathVariable UUID id, @RequestParam StatoViggio stato) {
        Optional<Viaggio> optionalViaggio = viaggioRepository.findById(id);
        if (optionalViaggio.isPresent()) {
            Viaggio viaggio = optionalViaggio.get();
            viaggio.setStato(stato);
            return ResponseEntity.ok(viaggioRepository.save(viaggio));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        viaggioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
