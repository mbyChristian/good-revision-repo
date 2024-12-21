import {inject, Injectable, signal} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {AuthResponse, Login, Register, User} from '../models/users.model';
import {Router} from '@angular/router';
import {API_URL} from '../app.config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http= inject(HttpClient)
  private baseUrl = `${API_URL}/auth`;
  private router =inject(Router)
  authStatus=signal<boolean>(this.hasToken())
  userinfo= signal<User |null>(this.getUserFromStorage())
  // Signal d'authentification
  isAuthenticatedSignal = signal<boolean>(this.hasToken());
  constructor() { }


  /**
   * Inscription d'un nouvel utilisateur
   */
  register(user: Register): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }
  /**
   * Vérifie si l'e-mail est disponible
   */
  checkEmailAvailability(email: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/checkEmailAvailability?email=${email}`);
  }

  /**
   * Vérifie si le username est disponible
   */
  checkUsernameAvailability(username: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/checkUsernameAvailability?username=${username}`);
  }
  /**
   * Connexion d'un utilisateur
   */
  login(credentials: Login): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, credentials)
      .pipe(
        tap(response => {
          if (response && response.token) {
            this.setSession(response);
            this.authStatus.set(true);
            this.userinfo.set(response.user)

          }
        })

      );

  }



  /**
   * Déconnexion de l'utilisateur
   */

  logout(): Observable<any> {
    return this.http.post(`${this.baseUrl}/logout`, {}, { responseType: 'text' as 'json' })
      .pipe(
        tap(() => {
          this.removeSession();
          this.authStatus.set(false);
          this.userinfo.set(null);
          this.router.navigate(['/home']);
        })
      );
  }

  /**
   * Stocke le token et les infos utilisateur dans le localStorage
   */
  private setSession(authResult: AuthResponse): void {
    localStorage.setItem('token', authResult.token);
    this.isAuthenticatedSignal.set(true)
    localStorage.setItem('user', JSON.stringify(authResult.user));
  }

  /**
   * Supprime le token du localStorage
   */
  private removeSession(): void {
    localStorage.removeItem('token');
    this.isAuthenticatedSignal.set(false)
    localStorage.removeItem('user');
  }

  /**
   * Récupère le token du localStorage
   */
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  /**
   * Vérifie si un token existe dans le localStorage
   */
  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  private getUserFromStorage(): User | null {
    const userData = localStorage.getItem('user');
    return userData ? JSON.parse(userData) as User : null;
  }

  /**
   * Récupère les rôles de l'utilisateur connecté
   */
  getUserRole(): string |undefined {
    const user = this.userinfo();
    return user?.role;
  }

  /**
   * Vérifie si l'utilisateur connecté est un ADMIN
   */
  isAdmin(): boolean |undefined {
    return this.getUserRole()?.includes('ADMIN');
  }
}
