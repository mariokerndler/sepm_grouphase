import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {Globals} from '../../../global/globals';

@Component({
  selector: 'app-commission-card',
  templateUrl: './commission-card.component.html',
  styleUrls: ['./commission-card.component.scss']
})
export class CommissionCardComponent implements OnInit {
  @Input() commission: SimpleCommissionDto;
  profilePicture;
  user: ApplicationUserDto;

  constructor(private userService: UserService, public globalFunctions: GlobalFunctions, public globals: Globals) {
  }

  ngOnInit(): void {
    this.fetchUser(this.commission.customerId);
  }

  private fetchUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (loadedUser) => {
        this.user = loadedUser;
        this.setProfilePicture();
      }
    });
  }

  private setProfilePicture() {
    if (!this.user.profilePictureDto) {
      this.profilePicture = this.globals.defaultProfilePicture;
    } else {
      this.profilePicture = this.user.profilePictureDto.imageUrl;
    }
  }
}
