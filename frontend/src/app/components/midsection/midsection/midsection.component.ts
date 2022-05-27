import {Component, Input, OnInit} from '@angular/core';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {ArtworkService} from '../../../services/artwork.service';
import {TagSearch} from '../../../dtos/tag-search';

@Component({
  selector: 'app-midsection',
  templateUrl: './midsection.component.html',
  styleUrls: ['./midsection.component.scss']
})
export class MidsectionComponent implements OnInit {

  @Input() artistId: number;
  @Input() columnCount = 5;
  @Input() imageAmount = 12;
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
    const tagSearch = {tagIds: [], searchOperations: '', pageNr: null} as TagSearch;
    this.artworkService.search(tagSearch).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks;
      }
    });
  }
}
