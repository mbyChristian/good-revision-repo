import { Component } from '@angular/core';
import {SubjectRepresentetionComponent} from '../../components/subject-representetion/subject-representetion.component';
import { StatistiquesComponent } from "../../components/statistiques/statistiques.component";

@Component({
  selector: 'app-apropos',
  imports: [
    StatistiquesComponent
],
  templateUrl: './apropos.component.html',
  styleUrl: './apropos.component.css'
})
export class AproposComponent {

}
