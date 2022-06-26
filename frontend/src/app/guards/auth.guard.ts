import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {NotificationService} from '../services/notification/notification.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const routeId = Number(route.paramMap.get('id'));
    if (this.authService.isLoggedIn() && this.authService.getUserId() === routeId) {
      return true;
    } else {
      this.notificationService.displayErrorSnackbar('Edit of another user\' profile page is prohibited.');
      this.router.navigate(['/feed'])
        .catch(_ => this.notificationService.displayErrorSnackbar('Could not load login page.'));
      return false;
    }
  }
}
