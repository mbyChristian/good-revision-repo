import {Component, HostListener, inject, OnInit} from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, NgForOf, NgIf } from '@angular/common';
import { Semestre } from '../../models/semestrel.model';
import { Matiere } from '../../models/matiere.model';
import {ActivatedRoute, Router} from '@angular/router';
import { SujetService } from '../../services/sujet.service';
import {MatDialog} from '@angular/material/dialog';
import {DialogSuccesComponent} from '../../components/dialog-succes/dialog-succes.component';

@Component({
  selector: 'app-create-subject',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './create-subject.component.html',
  styleUrls: ['./create-subject.component.css']
})
export class CreateSubjectComponent implements OnInit{
  private fb = inject(FormBuilder);
  private sujetService = inject(SujetService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private readonly dialog =inject(MatDialog)

  // Identifier si nous sommes en mode édition ou création
  isEditMode = false;
  editSubjectId: number | null = null;

  formGroup = this.fb.group({
    description: ['', Validators.required],
    pdf: [null as File|null, Validators.required],
    matiereId: [null as number|null, Validators.required],
    anneeId: [null as number|null, Validators.required],
    niveauId: [null as number|null, Validators.required]
  });


  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.isEditMode = true;
        this.editSubjectId = +idParam;
        this.loadSubjectData(this.editSubjectId);
      }
    });
  }


  loadSubjectData(id: number) {
    this.sujetService.get(id).subscribe({
      next: (subject) => {
        // On patch le formulaire avec les données du sujet
        this.formGroup.patchValue({
          description: subject.description,
          matiereId: subject.matiereId,
          anneeId: subject.anneeId,
          niveauId: subject.niveauId
        });

        // On met à jour la sélection affichée
        const selectedMatiereData = this.findMatiereById(subject.matiereId);
        if (selectedMatiereData) {
          this.selectedMatiere = selectedMatiereData.name;
          this.selectedMatiereId = selectedMatiereData.id;
        }

        const selectedAnneeData = this.annees.find(a => a.id === subject.anneeId);
        if (selectedAnneeData) {
          this.selectedYear = selectedAnneeData.year;
          this.selectedYearId = selectedAnneeData.id;
        }

        const selectedNiveauData = this.niveaux.find(n => n.id === subject.niveauId);
        if (selectedNiveauData) {
          this.selectedNiveau = selectedNiveauData.name;
          this.selectedNiveauId = selectedNiveauData.id;
        }
        this.formGroup.get('pdf')?.clearValidators();
        this.formGroup.get('pdf')?.updateValueAndValidity();
      },
      error: (err) => console.error('Erreur lors du chargement du sujet', err)
    });
  }

  onSubmit() {

    this.formGroup.markAllAsTouched();
    if (this.formGroup.valid) {
      const { description, pdf, matiereId, anneeId, niveauId } = this.formGroup.value;
      if (description && matiereId && anneeId && niveauId) {
        // En mode édition, pdf peut être null si on n'a pas re-sélectionné un fichier
        // On peut donc passer pdfFile facultativement
        const pdfFile = pdf instanceof File ? pdf : undefined;

        if (this.isEditMode && this.editSubjectId) {
          // Mode édition
          this.sujetService.update(this.editSubjectId, description, matiereId, anneeId, niveauId, pdfFile)
            .subscribe({
              next: () => {
                this.dialog.open(DialogSuccesComponent, {
                  data: 'Sujet modifié avec succès',
                });
              },
              error: (err) => console.error('Erreur lors de la mise à jour du sujet', err)
            });
        } else {
          // Mode création : pdf est requis (validé par le formulaire)
          if (pdfFile) {
            this.sujetService.add(description, matiereId, anneeId, niveauId, pdfFile)
              .subscribe({
                next: () => {
                  const dialogRef = this.dialog.open(DialogSuccesComponent, { data: 'Sujet créé avec succès' });
                  dialogRef.afterClosed().subscribe(() => {
                    this.formGroup.reset(); // Réinitialise le formulaire
                    // Optionnel : navigate away
                    // this.router.navigate(['/dashboard']);
                  });
                },
                error: (err) => console.error('Erreur lors de la création du sujet', err)
              });
          } else {
            console.log('Aucun fichier PDF sélectionné');
          }
        }

      } else {
        console.log('Formulaire invalide, données manquantes');
      }
    } else {
      console.log('Formulaire invalide');
    }
  }


  private findMatiereById(id: number) : Matiere | undefined {
    for (const semestre of this.semestres) {
      const matiereFound = semestre.matieres.find(m => m.id === id);
      if (matiereFound) return matiereFound;
    }
    return undefined;
  }

  navigateBack() {
    // Retour à la page précédente ou autre action
    this.router.navigate(['dashboard']);
  }


  // Données pour les matières par semestre
  semestres: Semestre[] = [
    {
      name: 'Semestre 1',
      matieres: [
        { name: 'Algèbre générale', id: 1 },
        { name: 'Electrostatique-Magnétostatique', id: 2 },
        { name: 'Droit et entreprise', id: 3 },
        { name: 'Chimie pour ingénieur 1', id: 4 },
        { name: 'Introduction à l\'algorithmique', id: 5 },
        { name: 'Communication et médias', id: 6 },
        { name: 'Analyse réelle 1', id: 7 },
        { name: 'Mécanique du point', id: 8 },
        { name: 'Optique géométrique et ondulatoire', id: 9 },
        { name: 'Géométrie descriptive', id: 10 },
        { name: 'Ethique et morale', id: 11 },
        { name: 'Langue française 1', id: 12 },
        { name: 'Langue anglaise 1', id: 13 }
      ]
    },
    {
      name: 'Semestre 2',
      matieres: [
        { name: 'Chimie pour ingénieur 2', id: 14 },
        { name: 'Matériaux 1', id: 15 },
        { name: 'Electrocinétique', id: 16 },
        { name: 'Analyse réelle 2', id: 17 },
        { name: 'Probabilités et statistique pour l\'ingénieur', id: 18 },
        { name: 'Initiation à la fabrication multi-technique 1', id: 19 },
        { name: 'Economie générale pour l\'ingénieur', id: 20 },
        { name: 'Introduction à la programmation', id: 21 },
        { name: 'Mécanique appliquée : statique', id: 22 },
        { name: 'Dessin technique', id: 23 },
        { name: 'Algèbre linéaire', id: 24 },
      ]
    },
    {
      name: 'Semestre 3',
      matieres: [
        { name: 'Matériaux 2', id: 25 },
        { name: 'Bases de Données', id: 26 },
        { name: 'Electromagnétisme', id: 27 },
        { name: 'Mécanique du solide', id: 28 },
        { name: 'Analyse des Données', id: 29 },
        { name: 'Techniques de Programmation', id: 30 },
        { name: 'Thermodynamiques', id: 31 },
        { name: 'Sociologie, éthique et innovation Technologique', id: 32 },
        { name: 'Algèbre Linéaire 2', id: 33 },
        { name: 'Séries', id: 34 },
        { name: 'Chimie pour Ingénieur 2', id: 35 },
        { name: 'DAO', id: 36 },
      ]
    },
    {
      name: 'Semestre 4',
      matieres: [
        { name: 'Introduction aux Outils de Calcul Scientifique', id: 37 },
        { name: 'Eléments de base en électronique', id: 38 },
        { name: 'Fonctions à plusieurs variables', id: 39 },
        { name: 'Introduction à la mécanique vibratoire', id: 40 },
        { name: 'Langue française 2', id: 41 },
        { name: 'Langue anglaise 2', id: 42 },
        { name: 'Comptabilité et gestion des entreprises', id: 43 },
        { name: 'Algèbre multilignéaire', id: 44 }
      ]
    }
  ];

  // Données pour les années
  annees = [
    { year: 2020, id: 1 },
    { year: 2021, id: 2 },
    { year: 2022, id: 3 },
    { year: 2023, id: 4 }
  ];

  // Données pour les niveaux
  niveaux = [
    { name: 'TCO1', id: 1 },
    { name: 'TCO2', id: 2 }
  ];

  // Variables d'état pour l'ouverture/fermeture des menus
  showMatiereDropdown = false;
  showAnneeDropdown = false;
  showNiveauDropdown = false;

  // Variables pour stocker la sélection
  selectedMatiere: string = '';
  selectedMatiereId: number | null = null;

  selectedYear: number | null = null;
  selectedYearId: number | null = null;

  selectedNiveau: string = '';
  selectedNiveauId: number | null = null;



  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.formGroup.get('pdf')?.setValue(file);
    }
  }



  toggleMatiereDropdown(event: MouseEvent) {
    event.stopPropagation();
    this.showMatiereDropdown = !this.showMatiereDropdown;
  }

  toggleAnneeDropdown(event: MouseEvent) {
    event.stopPropagation();
    this.showAnneeDropdown = !this.showAnneeDropdown;
  }

  toggleNiveauDropdown(event: MouseEvent) {
    event.stopPropagation();
    this.showNiveauDropdown = !this.showNiveauDropdown;
  }

  selectMatiere(matiere: Matiere) {
    this.selectedMatiere = matiere.name;
    this.selectedMatiereId = matiere.id;
    this.showMatiereDropdown = false;
    // On met à jour le formControl matiereId pour la validation
    this.formGroup.get('matiereId')?.setValue(matiere.id);
  }

  selectYear(annee: {year: number, id: number}) {
    this.selectedYear = annee.year;
    this.selectedYearId = annee.id;
    this.showAnneeDropdown = false;
    // On met à jour le formControl anneeId pour la validation
    this.formGroup.get('anneeId')?.setValue(annee.id);
  }

  selectNiveau(niveau: {name: string, id: number}) {
    this.selectedNiveau = niveau.name;
    this.selectedNiveauId = niveau.id;
    this.showNiveauDropdown = false;
    // On met à jour le formControl niveauId pour la validation
    this.formGroup.get('niveauId')?.setValue(niveau.id);
  }

  @HostListener('document:click')
  closeAllDropdowns() {
    this.showMatiereDropdown = false;
    this.showAnneeDropdown = false;
    this.showNiveauDropdown = false;
  }

  stopPropagation(event: MouseEvent) {
    event.stopPropagation();
  }
}


