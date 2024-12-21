import { Component, effect, inject } from '@angular/core';
import { Sujet } from '../../models/sujet.model';
import { NgForOf } from '@angular/common';
import { SujetService } from '../../services/sujet.service';
import { AuthService } from '../../services/auth.service';
import { SubjectRepresentetionComponent } from '../../components/subject-representetion/subject-representetion.component';

@Component({
  selector: 'app-list-subject',
  templateUrl: './list-subject.component.html',
  styleUrls: ['./list-subject.component.css'],
  standalone: true,
  imports: [
    NgForOf,
    SubjectRepresentetionComponent
  ]
})
export class ListSubjectComponent {
  subjects: Sujet[] = [];
  private sujetService = inject(SujetService);
  private authService = inject(AuthService);
  isConnected = false;

  constructor() {
    // Charge les sujets au démarrage
    this.sujetService.fetchSubjects();

    // Utilisation d'un effect pour réagir aux changements du signal subjects
    effect(() => {
      const data = this.sujetService.subjectsSource(); // Utiliser subjects() et non subjectsSource()
      this.subjects = data;
    });

    // Mise à jour de l'état de connexion
    effect(() => {
      this.isConnected = this.authService.isAuthenticatedSignal();
    });
  }

  onDownload(sujet: Sujet) {
    if (!this.isConnected) {
      alert('Vous devez être connecté pour télécharger ce sujet.');
      return;
    }

    this.sujetService.downloadSubject(sujet.id!).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = sujet.titre.replace(/\s+/g, '_') + '.pdf';
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erreur lors du téléchargement du fichier', err);
        alert('Une erreur est survenue lors du téléchargement.');
      }
    });
  }
}
