import {Component, Input, OnInit} from '@angular/core';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {Artist} from '../../../dtos/artist';
import {Artwork} from '../../../dtos/artwork';
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
  artworks: Artwork[];
  classifiedArtist: boolean;

  constructor(
    private fakerService: FakerGeneratorService,
    private artworkService: ArtworkService
  ) { }

  ngOnInit(): void {
    //const artist: Artist = this.fetchArtist(this.artistId);

    //this.artworks = this.fetchArtworks(artist ? artist.artworkIds : null);
    this.fetchArtworks(this.artistId);
    //this.searchFetchTest();
    this.classifiedArtist = (this.artistId != null);
  }

  private fetchArtist(artistId?: number): Artist {
    if(!artistId) {
      return null;
    } else {
      this.fakerService.generateFakeArtist(artistId, 1, 1).subscribe({
        next: (artist) => artist
      });
    }
  }
/*
  private oldFetchArtworks(artworkIds?: number[]): Artwork[] {
    let artworks: Artwork[] = [];

    if(!artworkIds) {
      this.fakerService.generateFakeArtworkByAmount(this.imageAmount).subscribe({
        next: (fakeArtworks) => {
          artworks = fakeArtworks;
        }
      });
    } else {
      for (const id of artworkIds) {
        this.fakerService.generateFakeArtwork(id).subscribe({
          next: (artwork) => {
            artworks.push(artwork);
          }
        });
      }
    }

    return artworks;
  }
*/
/*
  private searchFetchTest(){
    const search = {tagIds: [], searchOperations: '', pageNr: 1} as TagSearch;
    this.artworkService.search(search).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks;
      }
    });
  }
*/
  private fetchArtworks(artistId: number){
      this.artworkService.getArtworksByArtist(artistId).subscribe({
        next: (loadedArtworks) => {
          this.artworks = loadedArtworks;
        }
      });
  }
}
