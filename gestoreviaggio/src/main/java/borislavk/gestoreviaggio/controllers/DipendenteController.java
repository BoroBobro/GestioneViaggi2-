package borislavk.gestoreviaggio.controllers;

import borislavk.gestoreviaggio.entities.Dipendente;
import borislavk.gestoreviaggio.repositories.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @PostMapping
    public ResponseEntity<Dipendente> creaDipendente(@RequestBody @Validated Dipendente dipendente) {
        Dipendente salvato = dipendenteRepository.save(dipendente);
        return ResponseEntity.ok(salvato);
    }

    @GetMapping
    public List<Dipendente> getAll() {
        return dipendenteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dipendente> getById(@PathVariable UUID id) {
        return dipendenteRepository.findById(id).map((ResponseEntity::ok)).orElse(ResponseEntity.notFound().build());

    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        dipendenteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
