import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {
  AbstractControl, AbstractControlOptions,
  FormBuilder,
  FormGroup, FormsModule,
  ReactiveFormsModule,
  ValidationErrors, ValidatorFn,
  Validators
} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Register} from '../../models/users.model';
import {AuthService} from '../../services/auth.service';
import {MatSnackBar, MatSnackBarConfig} from '@angular/material/snack-bar';
import {catchError, map, Observable, of} from 'rxjs';

@Component({
  selector: 'app-inscription',
  standalone:true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule
  ],
  templateUrl: './inscription.component.html',
  styleUrl: './inscription.component.css'
})
export class InscriptionComponent implements OnInit{
  formGroup!:FormGroup
  isSubmitting: boolean = false;
  private router =inject(Router)
  private fb =inject(FormBuilder)
  private authService =inject(AuthService)
  private snackBar=inject(MatSnackBar)



  ngOnInit() {
    this.formGroup = this.fb.group({
      firstName: ['',[Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      username: ['', [Validators.required, Validators.minLength(2)],[this.usernameAvailabilityValidator()]],
      email: ['', Validators.required,[this.emailAvailabilityValidator()]],
      password: ['', [Validators.required, Validators.minLength(8),Validators.maxLength(20)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator } as AbstractControlOptions)
  }

  // Validateur pour vérifier que les mots de passe correspondent
  passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { 'mismatch': true };
  };

  /**
   * Méthodes getters pour accéder facilement aux contrôles du formulaire dans le template
   */
  get firstName() { return this.formGroup.get('firstName'); }
  get lastName() { return this.formGroup.get('lastName'); }
  get username() { return this.formGroup.get('username'); }
  get email() { return this.formGroup.get('email'); }
  get password() { return this.formGroup.get('password'); }
  get confirmPassword() { return this.formGroup.get('confirmPassword'); }

  /**
   * Gestion de la soumission du formulaire
   */
  onSubmit(): void {
    if (this.formGroup.invalid) {
      // Marquer tous les contrôles comme touchés pour afficher les messages d'erreur
      this.formGroup.markAllAsTouched();
      return;
    }

    this.isSubmitting = true; // Indique que la soumission est en cours

    const register: Register = {
      firstName: this.firstName?.value,
      lastName: this.lastName?.value,
      username: this.username?.value,
      email: this.email?.value,
      password: this.password?.value,
      confirmPassword: this.confirmPassword?.value,

    };

    this.authService.register(register).subscribe({
      next: (response) => {
        console.log('Inscription réussie'); // Log pour vérifier l'atteinte
        this.openSnackBar('Inscription réussie, vous pouvez maintenant vous connecter', 'Fermer','success');
        this.isSubmitting = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isSubmitting = false;
        const errorMsg = err.error.message || 'Une erreur est survenue lors de l\'inscription';
        this.openSnackBar(`Erreur : ${errorMsg}`, 'Fermer','error')
      }
    });
  }

  // Méthode pour ouvrir le snackbar
  openSnackBar(message: string, action: string, type : string) {
    let config = new MatSnackBarConfig(); // Crée une nouvelle config
    config.duration = 4000;
    config.horizontalPosition = 'end';
    config.verticalPosition = 'top';
    config.panelClass = [`snackbar-${type}`]

    this.snackBar.open(message, action,config);
  }

  navigateSignIn(){
    this.router.navigate(['login'])
  }



  // Validator pour verifier si un email est disponible
  emailAvailabilityValidator = () => {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if(!control.value) {
        return of(null);
      }
      return this.authService.checkEmailAvailability(control.value).pipe(
        map((response: {available : boolean}) => {
          if(!response.available) {
            return {emailTaken : true};
          }
          return null;
        }),
        catchError(() => of({emailTaken: true})) // si une erreur
      )
    };
  };
  // Validator pour verifier si un username est disponible
  usernameAvailabilityValidator = () => {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if(!control.value) {
        return of(null);
      }
      return this.authService.checkUsernameAvailability(control.value).pipe(
        map((response: {available : boolean}) => {
          if(!response.available) {
            return {usernameTaken : true};
          }
          return null;
        }),
        catchError(() => of({usernameTaken: true}))
      )
    };
  };
}


