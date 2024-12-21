package org.study.subjectresource.dto;


import lombok.Builder;
import org.study.subjectresource.entity.Annee;
import org.study.subjectresource.entity.Matiere;
import org.study.subjectresource.entity.Niveau;
import org.study.subjectresource.entity.Sujets;

@Builder
public record SubjectDtoMapper(
        Long id,
        String description,
        String pdfUrl,
        Long matiereId,
        Long anneeId,
        Long niveauId
) {

    public static Sujets toEntity(SubjectDtoMapper mapper, Matiere matiere, Niveau niveau, Annee  annee) {
        String titre = matiere.getNomMatiere() + " - " + niveau.getNomNiveau() + " - " + annee.getAnnee();
        return Sujets.builder()
                .id(mapper.id())
                .titre(titre)
                .description(mapper.description())
                .pdfUrl(mapper.pdfUrl())
                .matiere(matiere)
                .niveau(niveau)
                .annee(annee)
                .build();
    }
}
