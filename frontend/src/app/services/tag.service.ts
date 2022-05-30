import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {NotificationService} from './notification/notification.service';
import {Globals} from '../global/globals';
import {catchError, Observable} from 'rxjs';
import {TagDto} from '../dtos/tagDto';

@Injectable({
  providedIn: 'root'
})
export class TagService {

  private tagBaseUri: string = this.globals.backendUri + '/tags';
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
   * Fetches all tags stored in the system.
   *
   * @return Observable list of found tags.
   */
  getAllTags(): Observable<TagDto[]> {
    return this.http.get<TagDto[]>(this.tagBaseUri, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<TagDto[]>('Fetching all tags'))
      );
  }

  getImageTags(id: number): Observable<TagDto[]> {
    return this.http.get<TagDto[]>(this.tagBaseUri + '/' + id, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<TagDto[]>('Fetching all Tags for Image'))
      );
  }
}
