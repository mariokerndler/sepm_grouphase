import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute, NavigationStart, Router} from '@angular/router';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';
import {NotificationService} from '../../../services/notification/notification.service';
import {ArtistService} from '../../../services/artist.service';
import {ArtistProfileSettings} from '../artist-page-edit/artistProfileSettings';
import {AuthService} from '../../../services/auth.service';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {Location} from '@angular/common';
import {Globals} from '../../../global/globals';
import {NotificationDto, NotificationType} from '../../../dtos/notificationDto';
import {Sort} from '@angular/material/sort';

@Component({
  selector: 'app-artist-page',
  templateUrl: './artist-page.component.html',
  styleUrls: ['./artist-page.component.scss']
})
export class ArtistPageComponent implements OnInit, OnDestroy {
  artist: ArtistDto;
  user: ApplicationUserDto;
  profileSettings: ArtistProfileSettings;
  isReady = false;
  isArtist = false;
  canEdit = false;
  tabIndex = 0;
  profilePicture;

  hasReadNotifications = false;
  hasUnreadNotifications = false;
  notifications: NotificationDto[];
  sortedNotifications: NotificationDto[];
  readNotifications: NotificationDto[];

  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private artistService: ArtistService,
    private userService: UserService,
    private notificationService: NotificationService,
    private authService: AuthService,
    public globals: Globals
  ) {
  }

  private static compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  ngOnInit(): void {
    this.refreshOnBackButtonClick();
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => this.navigateToArtistList())
        .subscribe((user) => {
          this.user = user;

          this.setProfilePicture();

          this.fetchNotifications();
          this.fetchReadNotifications();

          if (this.user.userRole === UserRole.artist) {
            this.isArtist = true;

            this.artistService.getArtistById(params.id, () => this.navigateToArtistList())
              .subscribe((artist) => {
                this.artist = artist;

                if (this.artist.profileSettings) {
                  this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
                }

                this.canEdit = this.authService.getUserAuthEmail() === this.artist.email;
                this.isReady = true;
              });
          } else if (this.user.userRole === UserRole.user) {
            this.navigateToUserPage();
          }
        })
    );

    window.onload = () => {
      const reloading = sessionStorage.getItem('reloading');
      if (reloading) {
        sessionStorage.removeItem('reloading');
        this.changeIndex(1);
        this.notificationService.displaySuccessSnackbar('You successfully uploaded a new artwork');
      }
    };
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  refreshOnBackButtonClick(): void {
    this.router.events.subscribe((event: NavigationStart) => {
      if (event.navigationTrigger === 'popstate') {
        window.location.reload();
      }
    });
  }

  navigateToEdit() {
    this.router.navigate(['/artist', this.artist.id, 'edit'])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  navigateToCommission(id: number) {
    this.router.navigate(['/commissions', id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  navigateToUserPage() {
    this.router.navigate(['/user', this.user.id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  goBack() {
    this.location.back();
  }

  changeIndex($event: any) {
    this.tabIndex = $event;
  }

  sortNotifications(sort: Sort, read: boolean) {
    const data = read ? this.readNotifications.slice() : this.notifications.slice();
    if (!sort.active || sort.direction === '') {
      if (read) {
        this.readNotifications = data;
      } else {
        this.sortedNotifications = data;
      }
      return;
    }

    const sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'title':
          return ArtistPageComponent.compare(a.type, b.title, isAsc);
        case 'createdAt':
          return ArtistPageComponent.compare(a.createdAt.toString(), b.createdAt.toString(), isAsc);
        case 'type':
          return ArtistPageComponent.compare(a.type.toString(), b.type.toString(), isAsc);
        case 'referenceId':
          return ArtistPageComponent.compare(a.referenceId, b.referenceId, isAsc);
        default:
          return 0;
      }
    });

    if (read) {
      this.readNotifications = sortedData;
    } else {
      this.sortedNotifications = sortedData;
    }
  }

  convertNotificationTypeToString(type: NotificationType): string {
    const convertedType = type.toString().toLowerCase();
    let returnType = '';
    let first = true;
    for (const word of convertedType.split('_')) {
      if (first) {
        returnType += word.charAt(0).toUpperCase() + word.slice(1) + ' ';
        first = false;
      } else {
        returnType += word + ' ';
      }
    }

    return returnType.trim();
  }

  readNotification(notification: NotificationDto) {
    this.notificationService.pathNotificationIsReadOfIdFromUser(notification.id, notification.userId, true)
      .subscribe((_) => {
          this.fetchNotifications();
          this.fetchReadNotifications();
          this.notificationService.displaySuccessSnackbar('Marked notification as read.');
        }
      );
  }

  readAllNotifications() {
    this.notificationService.patchNotificationsIsRead(this.user.id, true)
      .subscribe(_ => {
          this.fetchNotifications();
          this.fetchReadNotifications();
          this.notificationService.displaySuccessSnackbar('Marked notifications as read.');
        }
      );
  }

  private navigateToArtistList() {
    this.router.navigate(['/artists'])
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

  private fetchNotifications() {
    this.notifications = [];

    this.notificationService.getUnreadNotificationsByUserId(this.user.id)
      .subscribe((notifications) => {
        this.notifications = notifications;
        this.hasUnreadNotifications = this.notifications.length > 0;
        this.sortedNotifications = this.notifications.slice();
      });
  }

  private fetchReadNotifications() {
    this.readNotifications = [];

    this.notificationService.getNotificationsByUserId(this.user.id)
      .subscribe((notifications) => {
        this.readNotifications = notifications.filter(notification => notification.read);
        this.hasReadNotifications = this.readNotifications.length > 0;
      });
  }
}
