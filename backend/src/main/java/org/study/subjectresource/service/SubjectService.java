package org.study.subjectresource.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.study.subjectresource.dto.SubjectDto;
import org.study.subjectresource.dto.SubjectDtoMapper;
import org.study.subjectresource.entity.Users;

import java.io.IOException;
import java.util.List;

@Service
public interface SubjectService {
     SubjectDto save(SubjectDtoMapper subjectDtoMapper, MultipartFile pdfFile) throws IOException;
     List<SubjectDto> findAll();
     SubjectDto findById(Long id);
     void deleteById(Long id);
     SubjectDto update(Long id,SubjectDtoMapper subjectDtoMapper, MultipartFile pdfFile) throws IOException;

     /**
      * Télécharge le fichier PDF associé à un sujet spécifique et l'associe à l'utilisateur actuel.
      *
      * @param subjectId l'ID du sujet dont le PDF doit être téléchargé
      * @param username l'utilisateur qui télécharge le sujet
      * @return une ressource représentant le fichier PDF
      * @throws IOException si une erreur survient lors de l'accès au fichier
      */

     // Méthode pour télécharger un sujet pour un utilisateur authentifié
     Resource downloadSubjectForUser(Long subjectId, String username) throws IOException;
     Long findAllDownloadSubject();
     Long findAllDownloadSubjectByUserId(Long userId);
     Long totalSujet();

}
