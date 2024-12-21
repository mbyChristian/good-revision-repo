
import {inject, Injectable, signal} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Sujet} from '../models/sujet.model';
import {API_URL} from '../app.config';

@Injectable({
  providedIn: 'root'
})
export class SujetService {
  private baseUrl = `${API_URL}/sujets`;
  private http =inject(HttpClient)
  // signal pour stocker la liste des sujets.
  subjectsSource = signal<Sujet[]>([]);
  totalsujet= signal<number>(0)
  totalSujetTelecharge =signal<number>(0)

  constructor() {}


  fetchSubjects(): void {
    this.http.get<Sujet[]>(`${this.baseUrl}`, ).subscribe({
      next: (data) => {
        console.log('Données reçues :', data);
        this.subjectsSource.set(data);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des sujets', err);
      }
    });
  }

  /**
   * Récupère tous les sujets
   */
  getAll(): Observable<Sujet[]> {
    return this.http.get<Sujet[]>(`${this.baseUrl}`);
  }

  /**
   * Récupère un sujet par son ID
   * @param id identifiant du sujet
   */
  get(id: number): Observable<Sujet> {
    return this.http.get<Sujet>(`${this.baseUrl}/${id}`);
  }
  /**
   * Crée un sujet
   * @param description description du sujet
   * @param matiereId ID de la matière
   * @param anneeId ID de l'année
   * @param niveauId ID du niveau
   * @param pdfFile Fichier PDF associé
   */
  // Pour l'ajout, au lieu de refetchSubjects(), on met à jour localement
  add(description: string, matiereId: number, anneeId: number, niveauId: number, pdfFile: File): Observable<Sujet> {
    const formData = new FormData();
    formData.append('description', description);
    formData.append('matiereId', matiereId.toString());
    formData.append('anneeId', anneeId.toString());
    formData.append('niveauId', niveauId.toString());
    formData.append('pdf', pdfFile);

    return this.http.post<Sujet>(this.baseUrl, formData).pipe(
      tap((newSujet) => {
        const current = this.subjectsSource();
        this.subjectsSource.set([...current, newSujet]);
      })
    );
  }

  /**
   * Met à jour un sujet
   * Note: Il faut un endpoint PUT sur votre backend pour supporter cette opération,
   * par exemple @PutMapping("/{id}") dans votre controller.
   *
   * @param id identifiant du sujet à mettre à jour
   * @param description nouvelle description
   * @param matiereId nouvel ID de la matière
   * @param anneeId nouvel ID de l'année
   * @param niveauId nouvel ID du niveau
   * @param pdfFile nouveau PDF (optionnel, si vous souhaitez mettre à jour le fichier)
   */


  update(id: number, description: string, matiereId: number, anneeId: number, niveauId: number, pdfFile?: File): Observable<Sujet> {
    const formData = new FormData();
    formData.append('description', description);
    formData.append('matiereId', matiereId.toString());
    formData.append('anneeId', anneeId.toString());
    formData.append('niveauId', niveauId.toString());

    if (pdfFile) {
      formData.append('pdf', pdfFile);
    }

    return this.http.put<Sujet>(`${this.baseUrl}/${id}`, formData).pipe(
      tap((updatedSujet) => {
        // Mettre à jour la liste locale
        const current = this.subjectsSource();
        const index = current.findIndex(s => s.id === id);
        if (index !== -1) {
          const updated = [...current];
          updated[index] = updatedSujet;
          this.subjectsSource.set(updated);
        }
      })
    );
  }

  /**
   * Supprime un sujet
   * @param id identifiant du sujet à supprimer
   */
  deleteSubject(id: number | undefined): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => {
        // Retirer le sujet localement
        const current = this.subjectsSource();
        this.subjectsSource.set(current.filter(s => s.id !== id));
      })
    );
  }
  /**
   * Télécharge le sujet par son ID
   * @param id identifiant du sujet
   * Cette méthode retourne un Observable<Blob> (un flux binaire)
   */
  // Dans votre SujetService
  downloadSubject(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${id}`, { responseType: 'blob' });
  }
  //methode pour recuperer tous les sujets telecharges
  fetchSubjectsTelecharges(): void {
    this.http.get<number>(`${this.baseUrl}/sujet-telecharges`).subscribe({
      next:(count)=>{
        this.totalSujetTelecharge.set(count)
      },
      error:(err)=>{
        console.error('Erreur lors de la recuperation du nombre de sujets telecharge')
      }
    })
  }

  //mathode qui retourne le nombre des sujets 
  fetchTotalSujet():void{
    this.http.get<number>(`${this.baseUrl}/total-sujet`).subscribe({
      next:(count)=>{
        this.totalsujet.set(count)
      },
      error:(err)=>{
        console.log('erreur lors de la recuperation du nombre des sujets');
        
      }
    })
  }



}

