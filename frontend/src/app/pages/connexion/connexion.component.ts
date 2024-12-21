import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from '@angular/router';
import {CommonModule, NgIf} from '@angular/common';
import {AuthService} from '../../services/auth.service';
import {Login} from '../../models/users.model';
import {MatSnackBar, MatSnackBarConfig} from '@angular/material/snack-bar';

@Component({
  selector: 'app-connexion',
  standalone:true,
  imports: [
    FormsModule,ReactiveFormsModule,
    NgIf,CommonModule

  ],
  templateUrl: './connexion.component.html',
  styleUrl: './connexion.component.css'
})
export class ConnexionComponent implements OnInit{
  private router =inject(Router)
  private fb =inject(FormBuilder)
  formGroup!:FormGroup
  private authService= inject(AuthService)
  isSubmitting: boolean = false;
  private snackBar=inject(MatSnackBar)
  loginFailed = false; // Nouveau pour gerer l'erreur de connexion

ngOnInit() {
  this.formGroup=this.fb.group({
    username:['',Validators.required],
    password:['',Validators.required]
  })
}

//  methode getter pour récuperer les formControl
  get username() { return this.formGroup.get('username'); }
  get password() { return this.formGroup.get('password'); }



  onSubmit(): void {
    if (this.formGroup.invalid) {
      this.formGroup.markAllAsTouched();
      return;
    }
    this.isSubmitting = true;
    this.loginFailed = false;

    const credentials: Login = {
      username: this.username?.value,
      password: this.password?.value
    };

    this.authService.login(credentials).subscribe({
      next: () => {
        setTimeout(() => {
          this.isSubmitting = false;
          // Vérifier les rôles de l'utilisateur
          if (this.authService.isAdmin()) {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/home']);
          }
        }, 1500);
      },
      error: (err) => {
        this.isSubmitting = false;
        const errorMsg = err.error.message || 'Une erreur est survenue lors de la connexion';
        console.log(errorMsg);
        this.loginFailed = true;
      }
    });
  }

  navigateRegister(){
    this.router.navigate(['register'])
  }
// Méthode pour ouvrir le snackbar
openSnackBar(message: string, action: string, type : string) {
  let config = new MatSnackBarConfig(); // Crée une nouvelle config
  config.duration = 3000; // 3 secondes
  config.horizontalPosition = 'end'; // Position à droite
  config.verticalPosition = 'top';   // Position en haut
  config.panelClass = [`snackbar-${type}`] // Ajoute la classe css pour la couleur

  this.snackBar.open(message, action,config);
}

}
