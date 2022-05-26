import { Injectable } from '@angular/core';
import {ApplicationUserDto} from '../dtos/applicationUserDto';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {NotificationService} from './notification/notification.service';
import {UserRole} from '../dtos/artistDto';

const backendUrl = 'http://localhost:8080';
const baseUri = backendUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  headers = new HttpHeaders({
  auth: 'frontend' });
  options = { headers: this.headers};


  constructor(
    private http: HttpClient,
    private notificationService: NotificationService) {}


  /**
   * Create a new {@link ApplicationUserDto user} and store it in the system
   *
   * @param user The {@link ApplicationUserDto} containing the information used to create a new user.
   * @param errorAction Optional, will execute if the POST request fails.
   * @param successAction Optional, will execute if the POST request succeeds.
   * @return observable containing the newly created {@link ApplicationUserDto}.
   */
  createUser(user: ApplicationUserDto, errorAction?: () => void, successAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.post<ApplicationUserDto>(baseUri, user, this.options)
        .pipe(
          catchError((err) => {
            if(errorAction != null) {
              errorAction();
            }

            return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Creating User')(err);
          }),
          tap(_ => {
            if(successAction != null) {
              successAction();
            }
          }));
  }

  /**
   * Fetches all users stored in the system
   *
   * @return observable list of found users.
   */
  getAll(): Observable<ApplicationUserDto[]> {
    return this.http.get<ApplicationUserDto[]>(baseUri, this.options);
  }

  /**
   * Fetches the {@link ApplicationUserDto user} with the given {@link ApplicationUserDto#id id} from the system.
   *
   * @param id The {@link ApplicationUserDto#id id} of the {@link ApplicationUserDto user} that will be fetched.
   * @param errorAction Optional, will execute if the GET request fails.
   *
   * @return Observable containing the fetched {@link ApplicationUserDto}.
   */
  getUserById(id: number, errorAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.get<ApplicationUserDto>(`${baseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if(errorAction != null){
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Finding user by id')(err);
        })
      );
  }


  /**
   * Update an already existing {@link ApplicationUserDto user}.
   *
   * @param user The {@link ApplicationUserDto} containing the information to update the specified user.
   * @param errorAction Optional, will execute if the PUT request fails.
   * @param successAction Optional, will execute if the PUT request succeeds.*
   * @return observable containing the newly created {@link ApplicationUserDto};
   */
  updateUser(user: ApplicationUserDto, errorAction?: () => void, successAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.put<ApplicationUserDto>(
      `${baseUri}`,
      user,
      this.options
    ).pipe(
      catchError((err) => {
        if(errorAction != null) {
          errorAction();
        }
        return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Editing User')(err);
      }),
      tap(_ => {
        if(successAction != null) {
          successAction();
        }
      }));
  }


}
