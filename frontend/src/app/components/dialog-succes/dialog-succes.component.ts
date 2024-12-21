import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from '@angular/material/dialog';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-dialog-succes',
  imports: [
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatButton,
    MatDialogTitle
  ],
  templateUrl: './dialog-succes.component.html',
  styleUrl: './dialog-succes.component.css'
})
export class DialogSuccesComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public message: string) {}

}
