import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {NotificationService} from './notification/notification.service';
import {catchError, Observable, tap} from 'rxjs';
import {ArtistDto} from '../dtos/artistDto';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class ArtistService {

  private artistBaseUri: string = this.globals.testingBackendUri + '/artist';
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};

  constructor(
    private http: HttpClient,
    private notificationService: NotificationService,
    private globals: Globals
  ) {
  }

  /**
   * Fetches all artists stored in the system
   *
   * @return observable list of found artists.
   */
  getAllArtists(): Observable<ArtistDto[]> {
    return this.http.get<ArtistDto[]>(this.artistBaseUri, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<ArtistDto[]>('Fetching all artists'))
      );
  }

  /**
   * Fetches the {@link ArtistDto artist} with the given {@link ArtistDto#id id} from the system.
   *
   * @param id The {@link ArtistDto#id id} of the {@link ArtistDto artist} that will be fetched.
   * @param errorNotification Optional, will execute if the GET request fails.
   *
   * @return Observable containing the fetched {@link ArtistDto}.
   */
  getArtistById(id: number, errorNotification?: () => void): Observable<ArtistDto> {
    return this.http.get<ArtistDto>(this.artistBaseUri + '/' + id, this.options)
      .pipe(
        catchError((err) => {
          if (errorNotification != null) {
            errorNotification();
          }

          return this.notificationService.notifyUserAboutFailedOperation<ArtistDto>('Fetching artist')(err);
        })
      );
  }

  /**
   * Update an already existing {@link ArtistDto artist}.
   *
   * @param artistDto The {@link ArtistDto} containing the information to update the specified artist.
   *
   * @return observable containing the newly created {@link ArtistDto};
   */
  updateArtist(artistDto: ArtistDto): Observable<ArtistDto> {
    return this.http.put<ArtistDto>(this.artistBaseUri, artistDto, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<ArtistDto>('Updating artist')),
        tap((_) => {
          this.notificationService.displaySuccessSnackbar(`Successfully updated "${artistDto.userName}"!`);
        })
      );
  }

  /**
   * Create a new {@link ArtistDto artist} and store it in the system
   *
   * @param artistDto The {@link ArtistDto} containing the information used to create a new artist.
   *
   * @return observable containing the newly created {@link ArtistDto}.
   */
  createArtist(artistDto: ArtistDto): Observable<ArtistDto> {
    return this.http.post<ArtistDto>(this.artistBaseUri, artistDto, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<ArtistDto>('Creating new artist')),
        tap((successArtist) => {
          this.notificationService.displaySuccessSnackbar(`Successfully created "${successArtist.userName}"!`);
        })
      );
  }
}
