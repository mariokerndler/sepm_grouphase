import {Component, Input, OnInit} from '@angular/core';
import {CommissionService} from '../../../services/commission.service';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {SearchConstraint} from '../../../global/SearchConstraint';

import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtistDto} from '../../../dtos/artistDto';


@Component({
  selector: 'app-commission-page',
  templateUrl: './commission-page.component.html',
  styleUrls: ['./commission-page.component.scss']
})
export class CommissionPageComponent implements OnInit {
  @Input()user: ApplicationUserDto;
  @Input()artist: ArtistDto;
  commissions: SimpleCommissionDto[];
  hasLoaded = false;
  searchCom: CommissionSearchDto= {
    date: SearchConstraint.asc, name: '', priceRange:[0,5000], userId:'',artistId:'',pageNr:0
  };
  constructor(private commissionService: CommissionService) { }

  ngOnInit(): void {
    if(this.user===undefined) {
      //search for artist  field
      this.searchCom.artistId=this.artist.id.toString();
    } else {
      //search for customer field
      this.searchCom.userId=this.user.id.toString();
    }
    this.searchCommissions();
  }
  public  searchCommissions(): void{
    //console.log(this.searchCom);
    this.commissionService.filterCommissions(this.searchCom).subscribe(data=>{
      this.commissions=data;
    });
  }
}
