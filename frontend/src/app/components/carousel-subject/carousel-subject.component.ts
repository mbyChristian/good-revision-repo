

import {Component, effect, inject, OnInit} from '@angular/core';
import {Sujet} from '../../models/sujet.model';
import {SujetService} from '../../services/sujet.service';
import {NgForOf} from '@angular/common';
import {AuthService} from '../../services/auth.service';
import {SubjectRepresentetionComponent} from '../subject-representetion/subject-representetion.component';
import {RouterLink, RouterLinkActive} from '@angular/router';


@Component({
  selector: 'app-carousel-subject',
  imports: [
    NgForOf,
    SubjectRepresentetionComponent,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './carousel-subject.component.html',
  styleUrl: './carousel-subject.component.css'
})
export class CarouselSubjectComponent {
  subjects: Sujet[]=[]
  reversedSubjects: Sujet[] = [];
  private sujetService =inject(SujetService)
  private authService =inject(AuthService)
  isConnected = false;

  constructor() {
    // Charge les sujets au démarrage
    this.sujetService.fetchSubjects();

    // Utilisation d'un effect pour réagir aux changements du signal subjects
    effect(() => {
      const data = this.sujetService.subjectsSource();
      this.subjects = data;
      // Récupère les 10 derniers sujets et les place en ordre inversé
      this.reversedSubjects = this.subjects.slice(-10).reverse();
    });
    //mise a jour de l'etat de connexion
    effect(() => {
      this.isConnected = this.authService.isAuthenticatedSignal();
    });
  }

  onDownload(sujet: Sujet | undefined) {
    if (!sujet?.id) return;
    if (!this.isConnected) {
      // L'utilisateur n'est pas connecté
      alert('Vous devez être connecté pour télécharger ce sujet.');
      return;
    }

    // L’utilisateur est connecté, on lance le téléchargement
    this.sujetService.downloadSubject(sujet.id).subscribe({
        next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        // Optionnel : si le sujet a un titre, l'utiliser comme nom de fichier
          const filename=sujet.titre.replace(/\+s/g,'_')+'pdf'
        a.download = filename;
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
