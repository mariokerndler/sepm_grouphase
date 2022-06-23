import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NotificationService} from '../../../services/notification/notification.service';
import {NotificationDto, NotificationType} from '../../../dtos/notificationDto';
import {Sort} from '@angular/material/sort';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {Router} from '@angular/router';

@Component({
  selector: 'app-notification-display',
  templateUrl: './notification-display.component.html',
  styleUrls: ['./notification-display.component.scss']
})
export class NotificationDisplayComponent implements OnInit {

  @Input() user: ApplicationUserDto;
  @Output() unreadEvent = new EventEmitter<boolean>();
  @Output() notificationLengthEvent = new EventEmitter<number>();

  hasUnreadNotifications = false;
  notifications: NotificationDto[];
  hasReadNotifications = false;
  sortedNotifications: NotificationDto[];
  readNotifications: NotificationDto[];

  constructor(
    private notificationService: NotificationService,
    private router: Router,
  ) { }

  private static compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  ngOnInit(): void {
    this.fetchNotifications();
    this.fetchReadNotifications();
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
          return NotificationDisplayComponent.compare(a.type, b.title, isAsc);
        case 'createdAt':
          return NotificationDisplayComponent.compare(a.createdAt.toString(), b.createdAt.toString(), isAsc);
        case 'type':
          return NotificationDisplayComponent.compare(a.type.toString(), b.type.toString(), isAsc);
        case 'referenceId':
          return NotificationDisplayComponent.compare(a.referenceId, b.referenceId, isAsc);
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

  navigateToCommission(id: number) {
    this.router.navigate(['/commissions', id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  private fetchNotifications() {
    this.notifications = [];

    this.notificationService.getUnreadNotificationsByUserId(this.user.id)
      .subscribe((notifications) => {
        this.notifications = notifications;
        this.hasUnreadNotifications = this.notifications.length > 0;
        this.unreadEvent.emit(this.hasUnreadNotifications);
        this.notificationLengthEvent.emit(this.notifications.length);

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
