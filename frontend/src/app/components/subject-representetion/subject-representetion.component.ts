import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Sujet} from '../../models/sujet.model';

@Component({
  selector: 'app-subject-representetion',
  imports: [],
  templateUrl: './subject-representetion.component.html',
  styleUrl: './subject-representetion.component.css'
})
export class SubjectRepresentetionComponent {
  @Input()sujet!:Sujet
  @Output()download=new EventEmitter<number>()
  onDownloadClick(){
    this.download.emit(this.sujet.id)
  }
}
