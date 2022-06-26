import {Component, Input, OnInit} from '@angular/core';
import {CommissionService} from '../../../services/commission.service';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {SearchConstraint} from '../../../global/SearchConstraint';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';
import {CommissionDto} from '../../../dtos/commissionDto';

@Component({
  selector: 'app-commission-page',
  templateUrl: './commission-page.component.html',
  styleUrls: ['./commission-page.component.scss']
})
export class CommissionPageComponent implements OnInit {
  @Input() user: ApplicationUserDto;
  @Input() artist: ArtistDto;
  commissions: CommissionDto[];
  hasLoaded = false;
  searchCom: CommissionSearchDto = {
    dateOrder: SearchConstraint.asc, name: '', priceRange: [0, 500000], userId: '', artistId: '', pageNr: 0
  };

  constructor(
    private commissionService: CommissionService) {
  }

  ngOnInit(): void {
    if (this.user.userRole === UserRole.artist) {
      //search for artist  field
      this.searchCom.artistId = this.user.id.toString();
    } else if (this.user.userRole === UserRole.user) {
      //search for customer field
      this.searchCom.userId = this.user.id.toString();
    }
    this.searchCommissions();
  }

  searchCommissions(): void {
    this.commissionService.filterDetailedCommissions(this.searchCom).subscribe(
      (data) => {
        this.commissions = data;
      });
  }
}
