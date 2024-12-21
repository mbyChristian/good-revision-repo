package org.study.subjectresource.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.subjectresource.dto.SubjectDtoMapper;
import org.study.subjectresource.dto.SubjectDto;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.repository.UsersRepo;
import org.study.subjectresource.service.SubjectService;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sujets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SubjectController {
    private final SubjectService subjectService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDto> save(@ModelAttribute SubjectDtoMapper subjectDtoMapper, @RequestParam("pdf") MultipartFile pdfFile) throws IOException {
        if(subjectDtoMapper.anneeId() == null || subjectDtoMapper.niveauId()==null || subjectDtoMapper.matiereId()==null) {
            throw new IllegalArgumentException("les champs anneeId matiereId niveauId sont obligatoires");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.save(subjectDtoMapper,pdfFile));
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAll());
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findById(id));
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable long id) {
        subjectService.deleteById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDto> update(@PathVariable Long id,
                                             @ModelAttribute SubjectDtoMapper subjectDtoMapper,
                                             @RequestParam(value = "pdf", required = false) MultipartFile pdfFile) throws IOException {
        if(subjectDtoMapper.anneeId() == null || subjectDtoMapper.niveauId()==null || subjectDtoMapper.matiereId()==null) {
            throw new IllegalArgumentException("les champs anneeId matiereId niveauId sont obligatoires");
        }
        SubjectDto updatedSubject = subjectService.update(id, subjectDtoMapper, pdfFile);
        return ResponseEntity.ok(updatedSubject);
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadSujet(@PathVariable Long id, Authentication authentication) throws IOException {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();

        Resource resource = subjectService.downloadSubjectForUser(id, username);

        String filename = (resource.getFilename() != null) ? resource.getFilename() : "fichier.pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);



    }

    @GetMapping("/sujet-telecharges")
    public ResponseEntity<Long> getAllDownloadSubjects(){
            return ResponseEntity.ok(subjectService.findAllDownloadSubject());
    }

    @GetMapping("/sujet-telecharges/userId")
    public ResponseEntity<Long> getAllDownloadSubjectByUserId(Authentication authentication){
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(subjectService.findAllDownloadSubjectByUserId(userId));
    }

    @GetMapping("/total-sujet")
    public ResponseEntity<Long> getTotalSujet(){
        return ResponseEntity.ok().body(subjectService.totalSujet());
    }
}
