import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';

//import * as data from 'src/assets/commission2.json';
import {CommissionService} from '../../../../services/commission.service';
import {ActivatedRoute} from '@angular/router';
import {CommissionDto} from '../../../../dtos/commissionDto';

@Component({
  selector: 'app-commission-timeline',
  templateUrl: './commission-timeline.component.html',
  styleUrls: ['./commission-timeline.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CommissionTimelineComponent implements OnInit {
  data;//: CommissionDto;
  isPlaying = false;
  isReady = false;
  newData: CommissionDto;

  constructor(private commissionService: CommissionService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    //this.data = data;
    this.getCommission();
  }

  getCommission(){
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe(
        (commission) => {
          //this.data = commission;
          this.newData = commission;
          console.log(this.newData);
          this.isReady = true;
        }
      );
  }

}
