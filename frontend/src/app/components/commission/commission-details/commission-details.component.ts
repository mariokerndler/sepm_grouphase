import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';
import {CommissionService} from '../../../services/commission.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-commission-details',
  templateUrl: './commission-details.component.html',
  styleUrls: ['./commission-details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionDetailsComponent implements OnInit {

  userProfilePicture = 'https://picsum.photos/150/150';
  commission: CommissionDto;
  user: ApplicationUserDto;
  hasLoaded = false;
  hasReferences = false;
  items = Array.from({length: 100000}).map((_, i) => `Item #${i}`);

  public selectedArtwork: number = null;

  constructor(private userService: UserService,
              private artworkService: ArtworkService,
              private commissionService: CommissionService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.getCommission();
  }


  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }


  private getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe((commission) => {
        this.commission = commission;
        this.user = commission.customerDto;
        this.hasLoaded = true;
        if(this.commission.referencesDtos.length !== 0){
          this.hasReferences = true;
        }
      });
  }
}
