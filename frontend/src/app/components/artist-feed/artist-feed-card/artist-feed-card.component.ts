import {Component, ElementRef, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ArtistDto} from '../../../dtos/artistDto';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {ArtworkService} from '../../../services/artwork.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {Globals} from '../../../global/globals';
import {Router} from '@angular/router';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {ArtistFeedComponent} from '../artist-feed.component';

@Component({
  selector: 'app-artist-feed-card',
  templateUrl: './artist-feed-card.component.html',
  styleUrls: ['./artist-feed-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ArtistFeedCardComponent implements OnInit {

  @Input() artist: ArtistDto;
  isReady = false;
  artworks: ArtworkDto[] = [];
  artistPfp;
  selectedArtwork: number = null;
  shown: boolean;

  constructor(
    private artworkService: ArtworkService,
    private notificationService: NotificationService,
    private router: Router,
    public globals: Globals,
    public globalFunctions: GlobalFunctions,
    {nativeElement}: ElementRef<HTMLImageElement>,
    private artistFeed: ArtistFeedComponent
  ) {
    const supports = 'loading' in HTMLImageElement.prototype;

    if (supports) {
      nativeElement.setAttribute('loading', 'lazy');
    }
  }

  ngOnInit(): void {
    this.shown = false;
    this.setArtistProfilePicture();

    this.artworkService.getArtworksByArtist(
      this.artist.id,
      () => this.notificationService.displayErrorSnackbar(`Could not load artist with username ${this.artist.userName}.`))
      .subscribe(
        (response) => {
          this.artworks = response;
          this.isReady = true;
        }
      );
  }

  navigateToArtist(artist: ArtistDto) {
    this.router.navigate(['/artist', artist.id])
      .catch((_) => this.notificationService.displayErrorSnackbar(`Could not navigate to the artist with username ${artist.userName}.`));
  }

  setSelectedArtwork(i: number) {
    this.shown = this.artistFeed.shown;
    if (!this.artistFeed.shown) {
      this.selectedArtwork = i;
      document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
      this.shown = this.artistFeed.blurBackground();
    }
  }

  resetSelectedArtwork() {
    this.resetShown();
    this.selectedArtwork = null;
  }

  resetShown() {
    this.artistFeed.resetShown();
  }

  private setArtistProfilePicture() {
    if (this.artist.profilePictureDto) {
      this.artistPfp = this.globals.assetsPath + this.artist.profilePictureDto.imageUrl;
    } else {
      this.artistPfp = this.globals.assetsPath + this.globals.defaultProfilePicture;
    }
  }

}
