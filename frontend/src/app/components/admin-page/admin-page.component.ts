import {Component, OnInit} from '@angular/core';
import {NotificationService} from '../../services/notification/notification.service';
import {UserService} from '../../services/user.service';
import {NotificationDto} from '../../dtos/notificationDto';
import {Globals} from '../../global/globals';
import {GlobalFunctions} from '../../global/globalFunctions';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit {

  hasNotifications = false;
  notifications: NotificationDto[] = [];

  constructor(
    private notificationService: NotificationService,
    private userService: UserService,
    private globals: Globals,
    public globalFunctions: GlobalFunctions
  ) {
  }

  ngOnInit(): void {
    this.fetchNotifications();
  }

  formatReportNotification(text: string): [string, string, string] {
    const parts = text.split('$$');
    return [parts[0], parts[1], parts[2]];
  }

  fetchNotifications() {
    this.notifications = [];
    this.notificationService.getNotificationsByUserId(this.globals.adminId)
      .subscribe(
        (notifications) => {
          notifications.forEach(x => {
            if (x) {
              if (x.type.includes('REPORT')) {
                this.notifications.push(x);
              }
            }
          });
          this.hasNotifications = this.notifications.length > 0;
        }
      );
  }
}
