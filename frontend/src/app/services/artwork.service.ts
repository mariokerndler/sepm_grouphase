import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {ArtworkDto} from '../dtos/artworkDto';
import {tap} from 'rxjs/operators';
import {TagSearch} from '../dtos/tag-search';
import {NotificationService} from './notification/notification.service';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ArtworkService {

  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};
  private artworkBaseUri: string = this.globals.backendUri + '/artworks';

  constructor(private http: HttpClient,
              private globals: Globals,
              private notificationService: NotificationService) {
  }

  /**
   * Fetches artworks that match the the given search-criteria from the system.
   *
   * @param tagSearch The {@link TagSearch tag-search } describe the search criteria that will be used to filter the database.
   * @param errorAction Optional, will execute if the GET request fails.
   *
   * @return Observable containing the fetched List of {@link ArtworkDto}.
   */

  search(tagSearch: TagSearch, errorAction?: () => void): Observable<ArtworkDto[]> {
    let searchOperations = tagSearch.searchOperations;
    if (!searchOperations) {
      searchOperations = '';
    }
    const params = new HttpParams()
      .set('tagIds', tagSearch.tagIds.toString())
      .set('artistIds', tagSearch.artistIds.toString())
      .set('searchOperations', searchOperations)
      .set('pageNr', tagSearch.pageNr == null ? '0' : tagSearch.pageNr)
      .set('randomSeed', tagSearch.randomSeed);
    // console.log(params.toString());
    const searchOptions = {
      headers: this.headers,
      params
    };
    return this.http.get<ArtworkDto[]>(this.artworkBaseUri, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ArtworkDto[]>('Searching artworks by tag')(err);
        })
      );
  }

  /**
   * Fetches the list of {@link ArtworkDto artworks} with the given {@link ArtistDto#id id} from the system.
   *
   * @param id The {@link ArtistDto#id id} of the list of {@link ArtworkDto artworks} that will be fetched.
   * @param errorAction Optional, will execute if the GET request fails.
   *
   * @return Observable containing the fetched list of {@link ArtworkDto}.
   */
  getArtworksByArtist(id: number, errorAction?: () => void): Observable<ArtworkDto[]> {
    return this.http.get<ArtworkDto[]>(`${this.artworkBaseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ArtworkDto[]>('Finding artworks by artist')(err);
        })
      );
  }

  /**
   * Deletes the {@link ArtworkDto artwork} with the given {@link ArtworkDto#id id} from the system.
   *
   * @param artwork the {@link ArtworkDto artwork} that will be deleted.
   *
   * @return Observable containing the fetched list of {@link ArtworkDto}.
   */
  deleteArtwork(artwork: ArtworkDto, successAction?: () => void): Observable<void> {
    const deleteOptions = {headers: this.headers, body: artwork};
    return this.http.delete<void>(`${this.artworkBaseUri}`, deleteOptions).pipe(
      tap(_ => {
        if (successAction != null) {
          successAction();
        }
      }));
  }

  /**
   * Create a new {@link ArtworkDto artwork} and store it in the system
   *
   * @param artwork The {@link ArtworkDto} containing the information used to create a new artwork.
   * @param errorAction Optional, will execute if the POST request fails.
   * @param successAction Optional, will execute if the POST request succeeds.
   * @return observable containing the newly created {@link ArtworkDto}.
   */
  createArtwork(artwork: ArtworkDto, errorAction?: () => void, successAction?: () => void): Observable<ArtworkDto> {
    return this.http.post<ArtworkDto>(this.artworkBaseUri, artwork, this.options)
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
