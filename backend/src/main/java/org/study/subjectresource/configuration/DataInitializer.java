package org.study.subjectresource.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.study.subjectresource.entity.Annee;
import org.study.subjectresource.entity.Matiere;
import org.study.subjectresource.entity.Niveau;
import org.study.subjectresource.repository.AnneeRepo;
import org.study.subjectresource.repository.MatiereRepo;
import org.study.subjectresource.repository.NiveauRepo;

//@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initData(AnneeRepo annee, NiveauRepo niveau, MatiereRepo matiere) {
        return args -> {
// Insertion des années
            annee.save(new Annee("2020"));
            annee.save(new Annee("2021"));
            annee.save(new Annee("2022"));
            annee.save(new Annee("2023"));
// Insertion des niveaux
            niveau.save(new Niveau("tco 1"));
            niveau.save(new Niveau("tco 2"));
// Insertion des matières niveau 1 semestre 1
            matiere.save(new Matiere("Algèbre générale"));
            matiere.save(new Matiere("Electrostatique-Magnétostatique"));
            matiere.save(new Matiere("Droit et entreprise"));
            matiere.save(new Matiere("Chimie pour ingénieur 1"));
            matiere.save(new Matiere("Introduction à l'algorithmique"));
            matiere.save(new Matiere("Communication et médias"));
            matiere.save(new Matiere("Analyse réelle 1"));
            matiere.save(new Matiere("Mécanique du point"));
            matiere.save(new Matiere("Optique géométrique et ondulatoire"));
            matiere.save(new Matiere("Géométrie descriptive"));
            matiere.save(new Matiere("Ethique et morale"));
            matiere.save(new Matiere("Langue française 1"));
            matiere.save(new Matiere("Langue anglaise 1"));

            // Insertion des matières niveau 1 semestre 2


            matiere.save(new Matiere("Chimie pour ingénieur 2"));
            matiere.save(new Matiere("Matériaux 1"));
            matiere.save(new Matiere("Electrocinétique"));
            matiere.save(new Matiere("Analyse réelle 2"));
            matiere.save(new Matiere("Probabilités et statistique"));
            matiere.save(new Matiere("Initiation à la fab multi-technique 1"));
            matiere.save(new Matiere("Economie générale pour l'ingénieur"));
            matiere.save(new Matiere("Introduction à la programmation"));
            matiere.save(new Matiere("Mécanique appliquée : statique"));
            matiere.save(new Matiere("Dessin technique"));
            matiere.save(new Matiere("Algèbre linéaire"));

            // Insertion des matières niveau 2 semestre 1

            matiere.save(new Matiere("Matenaux 2"));
            matiere.save(new Matiere("Bases de Données"));
            matiere.save(new Matiere("Electromagnétisme"));
            matiere.save(new Matiere("Mécanique du solide"));
            matiere.save(new Matiere("Analyse des Données"));
            matiere.save(new Matiere("Techniques de Programmation"));
            matiere.save(new Matiere("Thermodynamiques"));
            matiere.save(new Matiere(" Sociologie,ethique,innovation Technologique"));
            matiere.save(new Matiere("Algèbre Linéaire 2"));
            matiere.save(new Matiere(" Series"));
            matiere.save(new Matiere("Chimie pour Ingenieur 2"));
            matiere.save(new Matiere("DAO"));

            // Insertion des matières niveau 2 semestre 2

            matiere.save(new Matiere("Introducton aux Outils de Calcul Scientifique"));
            matiere.save(new Matiere("Eléments de base en électronique"));
            matiere.save(new Matiere("Fonctions à plusieurs varables"));
            matiere.save(new Matiere("Introduction à la mécanique vibratoire"));
            matiere.save(new Matiere("Langue française 2"));
            matiere.save(new Matiere("Langue anglaise 2"));
            matiere.save(new Matiere("Comptabilité et gestion des entrepnses"));
            matiere.save(new Matiere(" Algebre multilinéare"));




        };
    }
}