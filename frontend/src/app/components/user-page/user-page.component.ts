import {Component, OnDestroy, OnInit} from '@angular/core';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../services/user.service';
import {NotificationService} from '../../services/notification/notification.service';
import {AuthService} from '../../services/auth.service';
import {UserRole} from '../../dtos/artistDto';
import {Globals} from '../../global/globals';
import {ReportService, ReportType} from '../../services/report/report.service';
import {ChatDto} from '../../dtos/chatDto';
import {ChatService} from '../../services/chat-service';

@Component({
  selector: 'app-user-page',
  templateUrl: './user-page.component.html',
  styleUrls: ['../artist-page/artist-page/artist-page.component.scss']
})
export class UserPageComponent implements OnInit, OnDestroy {

  user: ApplicationUserDto;
  isReady = false;
  canEdit = false;
  profilePicture;
  hasUnreadNotifications: boolean;
  notificationLength: number;
  loggedInUserId;

  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private notificationService: NotificationService,
    private authService: AuthService,
    public globals: Globals,
    private reportService: ReportService,
    private chatService: ChatService
  ) {
  }

  private static navigateToUserList() {

  }

  ngOnInit(): void {
    this.loggedInUserId= Number.parseInt(localStorage.getItem('userId'), 10);
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => UserPageComponent.navigateToUserList())
        .subscribe((user) => {
          this.user = user;

          this.setProfilePicture();

          if (this.user.userRole === UserRole.artist) {
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

  setUnreadNotifications($event: boolean) {
    this.hasUnreadNotifications = $event;
  }

  setNotificationLength($event: number) {
    this.notificationLength = $event;
  }

  reportProfile() {
    this.reportService.openReportDialog(this.user.id, ReportType.user);
  }

  canReport(): boolean {
    if (!this.authService.isLoggedIn()) {
      return false;
    } else {
      return this.authService.getUserRole() !== 'ADMIN';
    }
  }

  messageUser() {
    const chat: ChatDto = {
      chatPartnerId: this.user.id, userId: this.loggedInUserId
    };
    this.chatService.postChat(chat).subscribe(success => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    }, error => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    });
  }

  private navigateToArtistPage() {
    this.router.navigate(['/artist', this.user.id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  private setProfilePicture() {
    if (!this.user.profilePictureDto) {
      this.profilePicture = this.globals.defaultProfilePicture;
    } else {
      this.profilePicture = this.user.profilePictureDto.imageUrl;
    }
  }
}
