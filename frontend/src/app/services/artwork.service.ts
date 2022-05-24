import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {NotificationService} from '../common/service/notification.service';
import {catchError, Observable} from 'rxjs';
import {Artwork} from '../dtos/artwork';
import {tap} from 'rxjs/operators';
import {TagSearch} from '../dtos/tag-search';

const backendUrl = 'http://localhost:8080';
const baseUri = backendUrl + '/artwork';


@Injectable({
  providedIn: 'root'
})
export class ArtworkService {
  headers = new HttpHeaders({
    auth: 'frontend'
  });
  options = {headers: this.headers};


  constructor(private http: HttpClient,
              private notificationService: NotificationService) {
  }

  search(tagSearch: TagSearch, errorAction?: () => void): Observable<Artwork[]> {
    return this.http.get<Artwork[]>(`${baseUri}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<Artwork[]>('Searching artworks by tag')(err);
        })
      );
  }


  getArtworksByArtist(id: number, errorAction?: () => void): Observable<Artwork[]> {
    return this.http.get<Artwork[]>(`${baseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<Artwork[]>('Finding artworks by artist')(err);
        })
      );
  }


  deleteArtist(id: number, artwork: Artwork): Observable<void> {
    const deleteOptions = {headers: this.headers, body: artwork};
    return this.http.delete<void>(`${baseUri}/${id}`, deleteOptions);
  }


  createArtwork(artwork: Artwork,errorAction?: () => void,successAction?: () => void): Observable<Artwork> {
    return this.http.post<Artwork>(baseUri, artwork, this.options)
      .pipe(
        catchError((err) => {
          if(errorAction != null) {
            errorAction();
          }

          return this.notificationService.notifyUserAboutFailedOperation<Artwork>('Creating Artwork')(err);
        }),
        tap(_ => {
          if(successAction != null) {
            successAction();
          }
        }));
  }


}
