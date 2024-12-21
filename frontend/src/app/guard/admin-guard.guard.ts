import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const adminGuardGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService)
  const router = inject(Router)


  if (authService.isAdmin()) {
   return true
  }
    router.navigate(['/home'])
  return false;
};
