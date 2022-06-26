import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommissionService} from '../../../services/commission.service';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {ArtistService} from '../../../services/artist.service';
import {ArtistDto} from '../../../dtos/artistDto';
import {LabelType, Options} from '@angular-slider/ngx-slider';
import {SearchConstraint} from '../../../global/SearchConstraint';
import {CommissionStatus} from '../../../global/CommissionStatus';
import {CommissionDto} from '../../../dtos/commissionDto';
import {Globals} from '../../../global/globals';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-commission-feed',
  templateUrl: './commission-feed.component.html',
  styleUrls: ['./commission-feed.component.scss']
})
export class CommissionFeedComponent implements OnInit {

  public searchEnum = SearchConstraint;
  searchDate: SearchConstraint;
  options: Options = {
    floor: 0,
    ceil: this.globals.maxCommissionPrice,
    translate: (value: number, label: LabelType): string => {
      switch (label) {
        case LabelType.Low:
          return '<b>Min price:</b> $' + value;
        case LabelType.High:
          return '<b>Max price:</b> $' + value;
        default:
          return '$' + value;
      }
    }
  };


  searchCom: CommissionSearchDto = {
    dateOrder: SearchConstraint.none,
    name: '',
    priceRange: [0, this.globals.maxCommissionPrice],
    artistId: '',
    userId: '',
    pageNr: 0
  };
  commissions: CommissionDto[];
  hasLoaded = false;
  artists: ArtistDto[];

  constructor(
    private commissionService: CommissionService,
    private authService: AuthService,
    private router: Router,
    private artistService: ArtistService,
    public globals: Globals) {
  }

  ngOnInit(): void {
    this.options.ceil = this.globals.maxCommissionPrice;
    this.fetchCommissions();
    this.loadAllArtists();
  }

  fetchCommissions() {
    this.commissionService.filterDetailedCommissions(this.searchCom).subscribe({
      next: (loadedCommissions) => {
        this.commissions = loadedCommissions;
        this.hasLoaded = true;
      }
    });
  }

  public search(): void {
    this.commissionService.filterDetailedCommissions(this.searchCom).subscribe(data => {
      this.commissions = data;
    });
  }

  public isListed(status: CommissionStatus): boolean {
    return status === CommissionStatus.listed;
  }

  public isLoggedIn(): boolean{
  if (this.authService.isLoggedIn()) {
    return true;
    }
  }

  private loadAllArtists() {
    this.artistService.getAllArtists().subscribe(data => {
      this.artists = data;
    });
  }
}
