import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {ArtworkDto} from '../dtos/artworkDto';
import {tap} from 'rxjs/operators';
import {TagSearch} from '../dtos/tag-search';
import {NotificationService} from './notification/notification.service';

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

  search(tagSearch: TagSearch, errorAction?: () => void): Observable<ArtworkDto[]> {
    return this.http.get<ArtworkDto[]>(`${baseUri}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ArtworkDto[]>('Searching artworks by tag')(err);
        })
      );
  }

  getArtworksByArtist(id: number, errorAction?: () => void): Observable<ArtworkDto[]> {
    return this.http.get<ArtworkDto[]>(`${baseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ArtworkDto[]>('Finding artworks by artist')(err);
        })
      );
  }

  deleteArtist(id: number, artwork: ArtworkDto): Observable<void> {
    const deleteOptions = {headers: this.headers, body: artwork};
    return this.http.delete<void>(`${baseUri}/${id}`, deleteOptions);
  }

  createArtwork(artwork: ArtworkDto, errorAction?: () => void, successAction?: () => void): Observable<ArtworkDto> {
    return this.http.post<ArtworkDto>(baseUri, artwork, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService.notifyUserAboutFailedOperation<ArtworkDto>('Creating Artwork')(err);
        }),
        tap(_ => {
          if (successAction != null) {
            successAction();
          }
        }));
  }


}
