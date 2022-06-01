import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {ArtistDto} from '../../../dtos/artistDto';
import {ArtistProfileSettings} from '../artist-page-edit/artistProfileSettings';
import {newArray} from '@angular/compiler/src/util';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';


@Component({
  selector: 'app-artist-information',
  templateUrl: './artist-information.component.html',
  styleUrls: ['./artist-information.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ArtistInformationComponent implements OnInit {

  @Input() artist: ArtistDto;
  @Output() tabIndexEvent = new EventEmitter<number>();
  profileSettings: ArtistProfileSettings;
  artworks: ArtworkDto[];
  isReady = false;
  public selectedArtwork: number = null;



  // TODO: Fill in the real profile picture
  artistUrl = 'https://picsum.photos/150/150';


  constructor(private artworkService: ArtworkService, public globalFunctions: GlobalFunctions,) {
  }

  ngOnInit(): void {
    if (this.artist.profileSettings) {
      this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
      console.log(this.profileSettings);
    }
    this.fetchArtworksFromUser(this.artist.id);

  }

  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }

  switchTab(index) {
    this.tabIndexEvent.emit(index);
  }

  private fetchArtworksFromUser(artistId: number) {
    this.artworkService.getArtworksByArtist(artistId).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks.slice(0,12);
        this.isReady = true;
      }
    });
  }

}
