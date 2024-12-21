import {Component, effect, inject, OnInit} from '@angular/core';
import {SujetService} from '../../services/sujet.service';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-statistiques',
  standalone:true,
  imports: [],
  templateUrl: './statistiques.component.html',
  styleUrl: './statistiques.component.css'
})
export class StatistiquesComponent {
  private sujetService = inject(SujetService);
  private userService =inject(UserService)
  totalSujets: number = 20;
  personnesInscrites: number = 2; // à implémenter plus tard
  sujetsTelecharges: number = 2;  // à implémenter plus tard

  constructor() {
    // Chargement initial du nombre d'utilisateurs
    this.userService.fetchUserCount();
    this.sujetService.fetchSubjectsTelecharges();
    this.sujetService.fetchTotalSujet();

    // Utilisation d'un effect pour réagir aux changements du signal subjects
    effect(() => {
      
      this.totalSujets=this.sujetService.totalsujet()
    });

    effect(() => {
      this.personnesInscrites = this.userService.totalUser();
    });

    effect(() => {
      this.sujetsTelecharges =this.sujetService.totalSujetTelecharge();
    });


    // Pour l'instant, valeurs fictives :
    this.personnesInscrites = 10;
    this.sujetsTelecharges = 10;
  }

}
