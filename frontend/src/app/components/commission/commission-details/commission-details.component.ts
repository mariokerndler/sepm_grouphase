import {Component, OnInit} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';
import {CommissionService} from '../../../services/commission.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-commission-details',
  templateUrl: './commission-details.component.html',
  styleUrls: ['./commission-details.component.scss']
})
export class CommissionDetailsComponent implements OnInit {
/*
  commission = {
    id: 1, artistId: null, customerId: 1, title: 'Commission Title',
    instructions: 'This is just a random description which describes some of the information within in the commission.' +
      'This is just a random description which describes some of the information ' +
      'within in the commission and now shows a more detailed description.\n' +
      ' Lorem ipsum dolor sit amet, consetetur sadipscing elitr, ' +
      'sed diam nonumy eirmod tempor invidunt ut labore et dolore ' +
      'magna aliquyam erat, sed diam voluptua. At vero eos et accusam ' +
      'et justo duo dolores et ea rebum. Stet clita kasd gubergren, no ' +
      'sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum ' +
      'dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod ' +
      'tempor invidunt.',
    sketchesShown: 0, feedbackSend: 0, comArtworkId: null, feedback: [], price: 300,
    issueDate: new Date(2022, 1, 1),
    deadlineDate: new Date(2022, 3, 1), referenceImageIds: [1, 2, 3]
  } as CommissionDto;
  */

  userProfilePicture = 'https://picsum.photos/150/150';
  commission: CommissionDto;
  user: ApplicationUserDto;
  hasLoaded = false;

  constructor(private userService: UserService,
              private artworkService: ArtworkService,
              private commissionService: CommissionService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.getCommission();
  }



  private getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe((commission) => {
        this.commission = commission;
        console.log(commission);
        this.user = commission.customerDto;
        this.hasLoaded = true;
      });
  }
}
