import { Component } from '@angular/core';
import {CarouselSubjectComponent} from '../../components/carousel-subject/carousel-subject.component';
import {StatistiquesComponent} from '../../components/statistiques/statistiques.component';
import {RouterLink, RouterLinkActive} from '@angular/router';



@Component({
  selector: 'app-home-page',
  imports: [
    CarouselSubjectComponent,
    StatistiquesComponent,
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {



}
