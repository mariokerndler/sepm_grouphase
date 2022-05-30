import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Globals} from '../../global/globals';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {RegistrationComponent} from '../registration/registration.component';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';
import {Router} from '@angular/router';
import {NotificationService} from '../../services/notification/notification.service';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userId: number;
  user: ApplicationUserDto;

  constructor(
    public authService: AuthService,
    public globals: Globals,
    public dialog: MatDialog,
    private notificationService: NotificationService,
    private router: Router) {
  }

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      this.userId = this.authService.getUserId();
    } else {
      this.userId = null;
    }
  }

  openDialog(isLogin: boolean) {
    if (isLogin) {
      this.dialog.open(LoginComponent);
    } else {
      this.dialog.open(RegistrationComponent);
    }
  }

  navigateToProfile() {
    this.router.navigate(['/artist', this.userId])
      .catch(_ => this.notificationService.displayErrorSnackbar('Could not navigate to user page.'));
  }

  logoutUser() {
    this.authService.logoutUser();
    this.router.navigate(['/feed'])
      .catch(_ => this.notificationService.displayErrorSnackbar('Could not navigate to frontpage.'));
  }
}
