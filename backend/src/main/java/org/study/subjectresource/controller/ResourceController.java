package org.study.subjectresource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.subjectresource.entity.Annee;
import org.study.subjectresource.entity.Matiere;
import org.study.subjectresource.entity.Niveau;
import org.study.subjectresource.repository.AnneeRepo;
import org.study.subjectresource.repository.MatiereRepo;
import org.study.subjectresource.repository.NiveauRepo;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final AnneeRepo anneeRepo;
    private final MatiereRepo matiereRepo;
    private final NiveauRepo niveauRepo;

    @PostMapping("/annee")
    public ResponseEntity<Annee> save(@RequestBody Annee annee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anneeRepo.save(annee));
    }
    @PostMapping("/niveau")
    public ResponseEntity<Niveau> save(@RequestBody Niveau niveau) {
        return ResponseEntity.status(HttpStatus.CREATED).body(niveauRepo.save(niveau));
    }
    @PostMapping("/matiere")
    public ResponseEntity<Matiere> save(@RequestBody Matiere matiere) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matiereRepo.save(matiere));
    }
}
