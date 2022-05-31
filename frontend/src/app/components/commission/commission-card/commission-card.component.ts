import {Component, Input, OnInit} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {Router} from '@angular/router';

@Component({
  selector: 'app-commission-card',
  templateUrl: './commission-card.component.html',
  styleUrls: ['./commission-card.component.scss']
})
export class CommissionCardComponent implements OnInit {
  @Input() commission: CommissionDto;
  userProfilePicture = 'https://picsum.photos/150/150';
  user: ApplicationUserDto;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.fetchUser(this.commission.userId);
  }


  private fetchUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (loadedUser) => {
        this.user = loadedUser;
      }
    });
  }

}
