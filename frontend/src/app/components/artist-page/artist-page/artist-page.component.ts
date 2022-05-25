import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {ArtistDto} from '../../../dtos/artistDto';
import {NotificationService} from '../../../services/notification/notification.service';
import {ArtistService} from '../../../services/artist.service';

@Component({
  selector: 'app-artist-page',
  templateUrl: './artist-page.component.html',
  styleUrls: ['./artist-page.component.scss']
})
export class ArtistPageComponent implements OnInit, OnDestroy {

  artist: ArtistDto;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private artistService: ArtistService,
    private notificationService: NotificationService
  ) { }

  private static navigateToArtistList() {
    console.log('FAILED');
  }

  ngOnInit(): void {
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.artistService.getArtistById(params.id, () => ArtistPageComponent.navigateToArtistList())
        .subscribe((artist) => {
          this.artist = artist;
        })
    );
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
