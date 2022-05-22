import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {Artist} from '../../../dtos/artist';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {NotificationService} from '../../../common/service/notification.service';

@Component({
  selector: 'app-artist-page',
  templateUrl: './artist-page.component.html',
  styleUrls: ['./artist-page.component.scss']
})
export class ArtistPageComponent implements OnInit, OnDestroy {

  artist: Artist;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fakerService: FakerGeneratorService,
    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    // TODO: Fetch real data
    this.routeSubscription = this.route.params
      .subscribe(params => this.fakerService
        .generateFakeArtist(1, 2, 5)
        .subscribe(artist => this.artist = artist));
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  navigateToEdit() {
    this.router.navigate(['/artist', this.artist.id, 'edit'])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }
}
