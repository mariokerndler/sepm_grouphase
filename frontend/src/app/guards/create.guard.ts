import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {AuthService} from '../services/auth.service';
import {NotificationService} from '../services/notification/notification.service';

@Injectable({
  providedIn: 'root'
})
export class CreateGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService) {
  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (this.authService.isLoggedIn()) {
      return true;
    } else {
      this.notificationService.displayErrorSnackbar('Creating a commission while not logged in is prohibited.');
      this.router.navigate(['/commissions'])
        .catch(_ => this.notificationService.displayErrorSnackbar('Could not load commission creation page.'));
      return false;
    }
  }
}
