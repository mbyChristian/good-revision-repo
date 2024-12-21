import {ChangeDetectorRef, Component, effect, inject, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {NgIf} from '@angular/common';
import {SearchComponent} from '../search/search.component';

@Component({
  selector: 'app-navigation-barre',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf
  ],
  templateUrl: './navigation-barre.component.html',
  styleUrl: './navigation-barre.component.css'
})
export class NavigationBarreComponent {
  private authService = inject(AuthService);


  isConnected = false;
  firstName = '';
  lastName = '';

  constructor() {

    effect(() => {
      this.isConnected = this.authService.authStatus();
      const user = this.authService.userinfo()
      if (user) {
        this.firstName = user.firstName;
        this.lastName = user.lastName
      } else {
        this.firstName = ''
        this.lastName = ''
      }
    });
  }

  isAdmin(): boolean |undefined {
    return this.authService.isAdmin();
  }
    logout(): void {
      this.authService.logout().subscribe({
        next: () => console.log('Déconnecté'),
        error: err => console.error('Erreur lors de la déconnexion :', err)
      });
    }

}
