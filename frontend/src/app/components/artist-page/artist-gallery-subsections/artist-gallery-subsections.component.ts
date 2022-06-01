import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtistDto} from '../../../dtos/artistDto';
import {TagDto} from '../../../dtos/tagDto';
import {TagSearch} from '../../../dtos/tag-search';

@Component({
  selector: 'app-artist-gallery-subsections',
  templateUrl: './artist-gallery-subsections.component.html',
  styleUrls: ['./artist-gallery-subsections.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ArtistGallerySubsectionsComponent implements OnInit {

  @Input() artist: ArtistDto;
  @Input() tags: TagDto[];
  artworks: ArtworkDto[];
  isReady = false;
  selectedArtwork: number = null;
  elementAmount = 12;

  constructor(private artworkService: ArtworkService) {
  }

  ngOnInit(): void {
    if (this.tags.length === 0) {
      this.fetchArtworksFromUser(this.artist.id);
    } else {
      this.fetchArtworksByTags();
    }
  }

  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }

  private fetchArtworksFromUser(artistId: number) {
    this.artworkService.getArtworksByArtist(artistId).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks;
        this.isReady = true;
      }
    });
  }

  private fetchArtworksByTags() {
    const tagNames = [];
    this.tags.forEach(item => {
      tagNames.push(item.id.toString());
    });
    this.artworkService.search(
      {
        tagIds: tagNames,
        artistIds: [this.artist.id.toString()],
        pageNr: 0,
        searchOperations: '',
        randomSeed: 0
      } as TagSearch
    ).subscribe({
      next: (loadedArtworks) => {
        this.artworks = loadedArtworks;
        this.isReady = true;
      }
    });
  }
}
