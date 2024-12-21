import {Component, EventEmitter, HostListener, model, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-search',
  imports: [
    FormsModule
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent {
  searchVisible = model(false);
  searchTerm = model<string>('');

  @Output() searchChange = new EventEmitter<string>();

  toggleSearch() {
    this.searchVisible.update(value => !value);
    if(this.searchVisible()){
      this.addClickListener();
    }
    else{
      this.removeClickListener();
    }
  }

  handleSearch() {
    this.searchChange.emit(this.searchTerm());
    // Logique de recherche ici (ex : appeler un service)
    console.log('Recherche pour:', this.searchTerm());
    // On pourrait aussi emettre l'evenement
  }

  onDocumentClick = (event: MouseEvent) => {
    if(!((event.target as HTMLElement).closest('.search-container') || (event.target as HTMLElement).closest('.fa-magnifying-glass'))){
      this.searchVisible.set(false)
      this.removeClickListener();
    }
  }

  addClickListener(): void {
    document.addEventListener('click',this.onDocumentClick);
  }
  removeClickListener():void{
    document.removeEventListener('click',this.onDocumentClick);
  }


  @HostListener('window:keyup', ['$event'])
  onKeyUp(event: KeyboardEvent) {
    if(event.key === 'Escape'){
      this.searchVisible.set(false);
    }
  }
}
