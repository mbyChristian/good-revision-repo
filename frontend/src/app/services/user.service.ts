import {inject, Injectable, Signal, signal, WritableSignal} from '@angular/core';
import {API_URL} from '../app.config';
import {Sujet} from '../models/sujet.model';
import {HttpClient} from '@angular/common/http';
import {catchError, of, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = `${API_URL}/users`;
  totalUser =signal<number>(0)
  private http =inject(HttpClient)
  constructor() { }

  fetchUserCount(): void {
    this.http.get<number>(`${this.baseUrl}/total`).subscribe({
      next: (count) => {
        this.totalUser.set(count);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération du nombre d\'utilisateurs', err);
      }
    });
  }
}
