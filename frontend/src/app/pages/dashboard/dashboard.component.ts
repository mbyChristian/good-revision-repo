
import {Component, effect, inject, OnInit} from '@angular/core';
import { SujetService } from '../../services/sujet.service';
import { Router } from '@angular/router';
import { Sujet } from '../../models/sujet.model';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { StatistiquesComponent } from '../../components/statistiques/statistiques.component';
import { MatDialog } from '@angular/material/dialog';
import { DialogDeleteComponent } from '../../components/dialog-delete/dialog-delete.component';

@Component({
  standalone: true,
  selector: 'app-list-subject',
  imports: [CommonModule, ReactiveFormsModule, StatistiquesComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  private sujetService = inject(SujetService);
  private router = inject(Router);
  private dialog = inject(MatDialog);

  subjects: Sujet[] = [];

  constructor() {
    // Charge les sujets au démarrage
    this.sujetService.fetchSubjects();

    // Utilisation d'un effect pour réagir aux changements du signal subjects
    effect(() => {
      this.subjects = this.sujetService.subjectsSource();
      // À chaque fois que ce signal change, cette fonction est ré-exécutée.
      // Plus besoin de subscribe
    });
  }

  onDeleteSubject(id: number | undefined) {
    const dialogRef = this.dialog.open(DialogDeleteComponent);

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // La suppression mettra automatiquement à jour le signal dans le service
        this.sujetService.deleteSubject(id).subscribe({
          error: (err) => console.error('Erreur lors de la suppression du sujet', err)
        });
      }
    });
  }

  onEditSubject(id: number | undefined) {
    this.router.navigate(['/sujet', id]);
  }

  onAddSubject() {
    this.router.navigate(['/sujet']);
  }
}
