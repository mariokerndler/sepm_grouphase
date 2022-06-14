import {Component, Input, OnInit} from '@angular/core';
import {CommissionService} from '../../../services/commission.service';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {SearchConstraint} from '../../../global/SearchConstraint';

import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';


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
    date: SearchConstraint.asc, name: '', priceRange:[0,500000], userId:'',artistId:'',pageNr:0
  };
  constructor(private commissionService: CommissionService) { }

  ngOnInit(): void {
    if(this.user.userRole===UserRole.artist) {
      //search for artist  field
      this.searchCom.artistId=this.user.id.toString();
      console.log('searching for '+this.searchCom.artistId+' as artist');
    } else if(this.user.userRole===UserRole.user){
      //search for customer field
      this.searchCom.userId=this.user.id.toString();
      console.log('searching for '+this.searchCom.userId+' as user');
    }
    this.searchCommissions();
  }
  public  searchCommissions(): void{
    //console.log(this.searchCom);
    this.commissionService.filterCommissions(this.searchCom).subscribe(data=>{
      console.log(this.searchCom);
      this.commissions=data;
    });
  }
}
