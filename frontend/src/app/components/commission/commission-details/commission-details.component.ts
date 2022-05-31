import {Component, OnInit} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';

@Component({
  selector: 'app-commission-details',
  templateUrl: './commission-details.component.html',
  styleUrls: ['./commission-details.component.scss']
})
export class CommissionDetailsComponent implements OnInit {

  commission = {
    id: 1, artistId: null, userId: 1, title: 'Commission Title',
    description: 'This is just a random description which describes some of the information within in the commission.' +
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
    startDate: new Date(2022, 1, 1),
    endDate: new Date(2022, 3, 1), referenceImageIds: [1, 2, 3]
  } as CommissionDto;

  userProfilePicture = 'https://picsum.photos/150/150';
  user: ApplicationUserDto;
  artworks;

  constructor(private userService: UserService, private artworkService: ArtworkService) {
  }

  ngOnInit(): void {
    this.fetchUser(this.commission.id);
  }


  /*
  private getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe(commission => this.commission = commission);
  }
  */

  private fetchUser(userId: number) {
    this.userService.getUserById(userId).subscribe({
      next: (loadedUser) => {
        this.user = loadedUser;
      }
    });
  }


  private fetchArtworks(userId: number) {
    this.artworks = Array(3);
    /*
    const ids = this.commission.referenceImageIds;
    for (let i = 0; i < ids.length; i++) {
      this.artworkService.getA(ids[i]).subscribe({
        next: (loadedUser) => {
          this.artworks[i] = loadedUser;
        }
      });
    }
    */
  }


}
