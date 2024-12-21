import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {provideAnimations} from '@angular/platform-browser/animations';
import {authInterceptor} from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes),provideHttpClient(), provideAnimationsAsync(),
    provideAnimations(),
    importProvidersFrom(MatSnackBarModule),
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};

export const API_URL = 'http://localhost:9000/api';


