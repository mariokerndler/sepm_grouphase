import {Component, OnDestroy, OnInit} from '@angular/core';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {NotificationService} from '../../services/notification/notification.service';
import {AuthService} from '../../services/auth.service';
import {UserRole} from '../../dtos/artistDto';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['../artist-page/artist-page/artist-page.component.scss']
})
export class UserPageComponent implements OnInit, OnDestroy {

  user: ApplicationUserDto;
  isReady = false;
  canEdit = false;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private notificationService: NotificationService,
    private authService: AuthService
  ) { }

  private static navigateToUserList() {
    // TODO: Implement a user list

  }

  ngOnInit(): void {
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => UserPageComponent.navigateToUserList())
        .subscribe((user) => {
          this.user = user;

          if(this.user.userRole === UserRole.artist) {
            this.navigateToArtistPage();
          }

          this.canEdit = this.authService.getUserAuthEmail() === this.user.email;
          this.isReady = true;
        })
    );
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  navigateToUserEditPage() {
    this.router.navigate(['/user', this.user.id, 'edit'])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  private navigateToArtistPage() {
    this.router.navigate(['/artist', this.user.id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }


}
