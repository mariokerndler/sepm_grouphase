import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';
import {GlobalFunctions} from '../../../global/globalFunctions';

@Component({
  selector: 'app-commission-card',
  templateUrl: './commission-card.component.html',
  styleUrls: ['./commission-card.component.scss']
})
export class CommissionCardComponent implements OnInit {
  @Input() commission: SimpleCommissionDto;
  userProfilePicture = 'https://picsum.photos/150/150';
  user: ApplicationUserDto;

  constructor(private userService: UserService, public globalFunctions: GlobalFunctions) {
  }

  ngOnInit(): void {
    this.fetchUser(this.commission.customerId);
  }

  private fetchUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (loadedUser) => {
        this.user = loadedUser;
      }
    });
  }

}
