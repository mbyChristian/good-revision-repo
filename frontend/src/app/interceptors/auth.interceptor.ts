
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Récupération du token depuis le localStorage
  const token = localStorage.getItem('token');

  let authReq = req;

  // Si un token existe, on clone la requête pour y ajouter l'en-tête Authorization
  if (token) {
    authReq = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  const router = inject(Router);
  const authService = inject(AuthService);

  return next(authReq).pipe(
    catchError(error => {
      if (error.status === 401) {
        // Le token est expiré, on effectue une déconnexion locale
        localStorage.removeItem('token');
        localStorage.removeItem('user');

        // Mettre à jour les signaux directement
        authService.isAuthenticatedSignal.set(false);
        authService.userinfo.set(null);
        authService.authStatus.set(false);

        // Redirige l'utilisateur vers la page de home
        router.navigate(['/home']);
      }
      return throwError(() => error);
    })
  );
};
