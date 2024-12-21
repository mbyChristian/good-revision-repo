package org.study.subjectresource.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sujets extends AbstractEntity {
    @Column(nullable = false)
    private String titre;
    @NotNull(message = "la description ne doit pas etre null")
    @NotEmpty(message = "la description ne doit pas etre vide")
    @NotBlank(message = "la description ne doit pas etre vide")
    private String description;

    @Column(nullable = false)
    private String pdfUrl;


    @ManyToMany(mappedBy = "sujetsTelecharges")
    private List<Users> user=new ArrayList<>();

    @ManyToOne
    private Matiere matiere;

    @ManyToOne
    private Niveau niveau;

    @ManyToOne
    private Annee annee;

}
