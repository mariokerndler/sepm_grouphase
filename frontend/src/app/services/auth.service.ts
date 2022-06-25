import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {catchError, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';
import {NotificationService} from './notification/notification.service';
import {ApplicationUserDto} from '../dtos/applicationUserDto';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(private httpClient: HttpClient,
              private globals: Globals,
              private notificationService: NotificationService) {
  }

  private static setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  private static getTokenExpirationDate(token: string): Date {
    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  private static setUserId(id: number) {
    localStorage.setItem('userId', String(id));
  }

  private static setUserRole(userRole: string) {
    localStorage.setItem('userRole', userRole);
  }

  private static setUserName(userName: string) {
    localStorage.setItem('userName', userName);
  }

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   * @param authorizedUser The applicationUserDto of the authorized user
   */
  loginUser(authRequest: AuthRequest, authorizedUser: ApplicationUserDto): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation('Login')),
        tap((authResponse: string) => {
          console.log(authResponse);
          AuthService.setToken(authResponse);
          AuthService.setUserId(authorizedUser.id);
          AuthService.setUserRole(authorizedUser.userRole);
          AuthService.setUserName(authorizedUser.userName);
          this.notificationService.displaySuccessSnackbar('Successfully logged in.');
        })
      );
  }

  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (AuthService.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  logoutUser() {
    console.log('Logout');
    localStorage.removeItem('authToken');
  }

  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  /**
   * Returns the user email based on the current token
   */
  getUserAuthEmail() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());

      return decoded.sub;
    }
  }

  getApplicationUserRole() {
    const role = localStorage.getItem('userRole');

    if (role != null) {
      return String(role);
    }
  }

  getUserId() {
    const id = localStorage.getItem('userId');

    if (id != null) {
      return Number(id);
    }
  }

  getUserName() {
    const userName = localStorage.getItem('userName');

    if (userName != null) {
      return userName;
    }
  }
}
