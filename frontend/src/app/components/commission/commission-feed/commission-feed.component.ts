import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommissionService} from '../../../services/commission.service';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {ArtistService} from '../../../services/artist.service';
import {ArtistDto} from '../../../dtos/artistDto';
import {LabelType, Options} from '@angular-slider/ngx-slider';
import {SearchConstraint} from '../../../global/SearchConstraint';

@Component({
  selector: 'app-commission-feed',
  templateUrl: './commission-feed.component.html',
  styleUrls: ['./commission-feed.component.scss']
})
export class CommissionFeedComponent implements OnInit {
  /*
    commission = {
      id: 1, artistId: null, customerId: 1, title: 'Commission Title',
      instructions: 'This is just a random description which describes some of the information within in the commission ',
      sketchesShown: 0, feedbackSend: 0, comArtworkId: null, feedback: [], price: 300,
      issueDate: new Date(2022, 1, 1),
      deadlineDate: new Date(2022, 3, 1), referenceImageIds: [1, 2, 3]
    } as CommissionDto; */
  public searchEnum= SearchConstraint;
  options: Options = {
    floor: 0,
    ceil: 10000,
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


  searchCom: CommissionSearchDto= {
    date: SearchConstraint.none, name: '', priceRange:[0,5000], artistId:'',userId:'',pageNr:0
  };
  commissions: SimpleCommissionDto[];
  hasLoaded = false;
  artists: ArtistDto[];

  constructor(private commissionService: CommissionService, private router: Router, private artistService: ArtistService) {
  }

  ngOnInit(): void {
    this.fetchCommissions();
    this.loadAllArtists();
  }
  public  search(): void{
    console.log(this.searchCom);
   this.commissionService.filterCommissions(this.searchCom).subscribe(data=>{
      this.commissions=data;
    });
  }

  public routeToCommissionCreation(): void {
    this.router.navigate(['/commisson-create']);
  }
  private  loadAllArtists(){
    this.artistService.getAllArtists().subscribe(data=> {
    this.artists=data;
    });
  }
  private fetchCommissions() {
    this.commissionService.getAllCommissions().subscribe({
      next: (loadedCommissions) => {
        this.commissions = loadedCommissions;
        console.log(this.commissions);
        this.hasLoaded = true;
      }
    });
  }
}
