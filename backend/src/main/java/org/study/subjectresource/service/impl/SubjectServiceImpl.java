package org.study.subjectresource.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.study.subjectresource.dto.SubjectDtoMapper;
import org.study.subjectresource.dto.SubjectDto;
import org.study.subjectresource.entity.*;
import org.study.subjectresource.repository.*;
import org.study.subjectresource.service.SubjectService;
import org.study.subjectresource.validators.ObjectsValidator;

import javax.security.auth.Subject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepo subjectRepo;
    private final MatiereRepo matiereRepo;
    private final NiveauRepo niveauRepo;
    private final AnneeRepo anneeRepo;
    private final ObjectsValidator<SubjectDtoMapper> subjectValidator;
    private final ObjectsValidator<Sujets> validator;
    private final UsersRepo usersRepo;
    private final String uploadDirectory = "%s\\src\\main\\resources\\pdf_folder\\".formatted(System.getProperty("user.dir"));


    @Override
    public SubjectDto save(SubjectDtoMapper subjectDtoMapper, MultipartFile pdfFile) throws IOException {
        // Vérifier si le fichier est bien un PDF
        String fileName = pdfFile.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("veuillez selectionner un fichier au format PDF");
        }
        // Gestion de l'upload du fichier PDF
        Path path = Paths.get(uploadDirectory + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, pdfFile.getBytes());
        // Générer l'URL du fichier PDF
        String pdfUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/pdf_folder/")
                .path(fileName)
                .toUriString();

        // Charger les entités associées à partir des identifiants
        Matiere matiere = matiereRepo.findById(subjectDtoMapper.matiereId())
                .orElseThrow(()-> new IllegalArgumentException("matiere n'existe pas"));
        Annee annee = anneeRepo.findById(subjectDtoMapper.anneeId())
                .orElseThrow(() -> new IllegalArgumentException("Année non trouvée"));
        Niveau niveau = niveauRepo.findById(subjectDtoMapper.niveauId())
                .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));

        // Mapper le DTO à une entité Subject
        Sujets subject = SubjectDtoMapper.toEntity(subjectDtoMapper, matiere, niveau, annee);
        subject.setPdfUrl(pdfUrl);
        //validation
        validator.validate(subject);
        // Sauvegarder l'entité Subject
        Sujets savedSubject = subjectRepo.save(subject);
        // Retourner le SubjectDto correspondant
        return SubjectDto.toDto(savedSubject);
    }

    @Override
    public List<SubjectDto> findAll() {
        return subjectRepo.findAll().stream().map(SubjectDto::toDto).toList();
    }

    @Override
    public SubjectDto findById(Long id) {
        Optional<Sujets> subject = subjectRepo.findById(id);
        return subject.map(SubjectDto::toDto).orElseThrow(()-> new EntityNotFoundException("un sujet avec cet id n'existe pas "+id));
    }

    @Override
    public void deleteById(Long id) {
        subjectRepo.deleteById(id);
    }




    @Override
    public SubjectDto update(Long id, SubjectDtoMapper subjectDtoMapper, MultipartFile pdfFile) throws IOException {
        // Vérifier si le sujet existe
        Sujets existingSubject = subjectRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sujet non trouvé avec l'ID : " + id));

        // Vérifier si un fichier PDF est fourni pour mise à jour
        String pdfUrl = existingSubject.getPdfUrl(); // Conserver l'URL existante par défaut
        if (pdfFile != null && !pdfFile.isEmpty()) {
            String fileName = pdfFile.getOriginalFilename();
            if (fileName == null || !fileName.toLowerCase().endsWith(".pdf")) {
                throw new IllegalArgumentException("Veuillez sélectionner un fichier au format PDF");
            }

            // Gestion de l'upload du fichier PDF
            Path path = Paths.get(uploadDirectory + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, pdfFile.getBytes());

            // Générer l'URL du nouveau fichier PDF
            pdfUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/pdf_folder/")
                    .path(fileName)
                    .toUriString();
        }

        // Charger les entités associées à partir des identifiants
        Matiere matiere = matiereRepo.findById(subjectDtoMapper.matiereId())
                .orElseThrow(() -> new IllegalArgumentException("Matière non trouvée"));
        Annee annee = anneeRepo.findById(subjectDtoMapper.anneeId())
                .orElseThrow(() -> new IllegalArgumentException("Année non trouvée"));
        Niveau niveau = niveauRepo.findById(subjectDtoMapper.niveauId())
                .orElseThrow(() -> new IllegalArgumentException("Niveau non trouvé"));

        // Mapper le DTO à l'entité existante
        String titre = matiere.getNomMatiere() + " - " + niveau.getNomNiveau() + " - " + annee.getAnnee();
        existingSubject.setTitre(titre);
        existingSubject.setDescription(subjectDtoMapper.description());
        existingSubject.setPdfUrl(pdfUrl);
        existingSubject.setMatiere(matiere);
        existingSubject.setNiveau(niveau);
        existingSubject.setAnnee(annee);

        // Validation
        validator.validate(existingSubject);

        // Sauvegarder les modifications
        Sujets updatedSubject = subjectRepo.save(existingSubject);

        // Retourner le DTO mis à jour
        return SubjectDto.toDto(updatedSubject);
    }

    @Override
    @Transactional
    public Resource downloadSubjectForUser(Long subjectId, String username) throws IOException {
        Users user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + username));

        Sujets sujet = subjectRepo.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Sujet non trouvé avec l'ID : " + subjectId));

        String pdfUrl = sujet.getPdfUrl();
        String fileName;
        try {
            URL url = new URL(pdfUrl);
            String decodedPath = java.net.URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            fileName = Paths.get(decodedPath).getFileName().toString();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL PDF invalide : " + pdfUrl, e);
        }

        Path filePath = Paths.get(uploadDirectory).resolve(fileName).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new IllegalArgumentException("Fichier PDF non trouvé : " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Erreur lors de la lecture du fichier PDF : " + fileName, e);
        }

        // Mettre à jour la relation côté Users (propriétaire)
        if (!user.getSujetsTelecharges().contains(sujet)) {
            user.getSujetsTelecharges().add(sujet);
            usersRepo.save(user);
        }

        return resource;
    }

    @Override
    public Long findAllDownloadSubject() {
        List<Sujets> sujets = subjectRepo.allSubjectDownload();
        return (long) sujets.size();
    }

    @Override
    public Long findAllDownloadSubjectByUserId(Long userId) {
        Users users =usersRepo.findById(userId).orElseThrow(()-> new EntityNotFoundException("utilisateur pas existant avec cet id : " + userId));
        List<Sujets> sujets =users.getSujetsTelecharges();
        return (long) sujets.size();
    }

    @Override
    public Long totalSujet() {
        return  subjectRepo.count();
    }


}



