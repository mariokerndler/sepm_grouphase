import {Component, Input, OnInit} from '@angular/core';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {ArtworkService} from '../../../services/artwork.service';

@Component({
  selector: 'app-midsection',
  templateUrl: './midsection.component.html',
  styleUrls: ['./midsection.component.scss']
})
export class MidsectionComponent implements OnInit {

  @Input() artistId: number;
  @Input() columnCount = 5;
  artworks: ArtworkDto[];

  constructor(
    private artworkService: ArtworkService
  ) { }

  ngOnInit(): void {
    this.fetchArtworks(this.artistId);
  }

  private fetchArtworks(artistId?: number) {
    if(artistId) {
      this.fetchArtworksFromUser(artistId);
    } else {
      this.fetchArtworksFromAllUsers();
    }
  }

  private fetchArtworksFromUser(artistId: number) {
    this.artworkService.getArtworksByArtist(artistId).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks;
      }
    });
  }

  private fetchArtworksFromAllUsers() {
    // TODO: Fetch artworks from different users.
  }
}
