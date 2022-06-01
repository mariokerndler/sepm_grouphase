import {Component, ElementRef, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ArtistDto} from '../../../dtos/artistDto';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {ArtworkService} from '../../../services/artwork.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {Globals} from '../../../global/globals';
import {Router} from '@angular/router';
import {GlobalFunctions} from '../../../global/globalFunctions';

@Component({
  selector: 'app-artist-feed-card',
  templateUrl: './artist-feed-card.component.html',
  styleUrls: ['./artist-feed-card.component.scss'],

})
export class ArtistFeedCardComponent implements OnInit {

  @Input() artist: ArtistDto;
  isReady = false;
  artworks: ArtworkDto[] = [];
  artistPfp = 'https://picsum.photos/150/150';
  private maxArtworkLoad = 8;

  constructor(
    private artworkService: ArtworkService,
    private notificationService: NotificationService,
    private router: Router,
    public globals: Globals,
    public globalsFunctions: GlobalFunctions,
    {nativeElement}: ElementRef<HTMLImageElement>
  ) {
    const supports = 'loading' in HTMLImageElement.prototype;

    if (supports) {
      nativeElement.setAttribute('loading', 'lazy');
    }
  }

  ngOnInit(): void {
    this.artworkService.getArtworksByArtist(
      this.artist.id,
      () => this.notificationService.displayErrorSnackbar(`Could not load artist with username ${this.artist.userName}.`))
      .subscribe(
        (response) => {
          this.artworks = response.splice(0, this.maxArtworkLoad);
          this.isReady = true;
        }
      );
  }

  navigateToArtist(artist: ArtistDto) {
    this.router.navigate(['/artist', artist.id])
      .catch((_) => this.notificationService.displayErrorSnackbar(`Could not navigate to the artist with username ${artist.userName}.`));
  }

}
