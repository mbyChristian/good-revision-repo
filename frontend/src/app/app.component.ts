import {Component, effect, inject} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {CarouselSubjectComponent} from './components/carousel-subject/carousel-subject.component';
import {NavigationBarreComponent} from './components/navigation-barre/navigation-barre.component';
import {NgIf} from '@angular/common';
import {filter} from 'rxjs';
import {FooterComponent} from './components/footer/footer.component';




@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavigationBarreComponent, NgIf, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  isAdminRoute= false
  private router =inject(Router)
  constructor() {
    this.router.events.pipe(filter((event)=>event instanceof
     NavigationEnd)).subscribe((event:
    NavigationEnd)=>{
      this.isAdminRoute = [ "/login", "/register","/sujet"].some(path => event.url.startsWith(path));

    })
  }


}
