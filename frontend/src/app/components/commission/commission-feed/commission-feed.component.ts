import {Component, OnInit} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {Router} from '@angular/router';

@Component({
  selector: 'app-commission-feed',
  templateUrl: './commission-feed.component.html',
  styleUrls: ['./commission-feed.component.scss']
})
export class CommissionFeedComponent implements OnInit {

  commission = {
    id: 1, artistId: null, userId: 1, title: 'Commission Title',
    description: 'This is just a random description which describes some of the information within in the commission ',
    sketchesShown: 0, feedbackSend: 0, comArtworkId: null, feedback: [], price: 300,
    startDate: new Date(2022, 1, 1),
    endDate: new Date(2022, 3, 1), referenceImageIds: [1, 2, 3]
  } as CommissionDto;

  constructor(private  router: Router) {
  }

  ngOnInit(): void {
  }


  public routeToCommissionCreation(): void{
    this.router.navigate(['/commisson-create']);
  }

  numSequence(n: number): Array<number> {
    return Array(n);
  }

}
