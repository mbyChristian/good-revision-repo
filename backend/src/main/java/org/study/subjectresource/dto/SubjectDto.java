package org.study.subjectresource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.study.subjectresource.entity.Sujets;

@Builder
public record SubjectDto(
        Long id,
        String titre,
        @NotNull(message = "ne doit pas etre null")
        @NotEmpty(message = "ne doit pas etre null")
        @NotBlank(message = "ne doit pas etre null")
        String description,
        String pdfUrl,
        Long matiereId,
        Long anneeId,
        Long niveauId
) {

    public static SubjectDto toDto(Sujets subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getTitre(),
                subject.getDescription(),
                subject.getPdfUrl(),
                subject.getMatiere() != null ? subject.getMatiere().getId() : null,
                subject.getAnnee() != null ? subject.getAnnee().getId() : null,
                subject.getNiveau() != null ? subject.getNiveau().getId() : null
        );
    }
}
