import {Component, OnInit} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {Router} from '@angular/router';
import {CommissionService} from '../../../services/commission.service';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';

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

  commissions: SimpleCommissionDto[];
  hasLoaded = false;

  constructor(private commissionService: CommissionService, private  router: Router){
  }

  ngOnInit(): void {
    this.fetchCommissions();
  }

  public routeToCommissionCreation(): void{
    this.router.navigate(['/commisson-create']);
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
