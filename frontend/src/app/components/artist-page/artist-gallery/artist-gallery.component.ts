import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../../../services/artwork.service';
import {UploadComponent} from '../../upload/upload.component';
import {MatDialog} from '@angular/material/dialog';
import {ArtworkDto} from '../../../dtos/artworkDto';

@Component({
  selector: 'app-artist-gallery',
  templateUrl: './artist-gallery.component.html',
  styleUrls: ['./artist-gallery.component.scss']
})
export class ArtistGalleryComponent implements OnInit {

  @Input() artist;
  artworks: ArtworkDto[] = [];
  isReady = false;
  artistProfilePicture: string;

  constructor(
    private artworkService: ArtworkService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.artistProfilePicture = 'https://picsum.photos/100/100';

    this.artworkService.getArtworksByArtist(this.artist.id)
      .subscribe(
        (artworks) => {
          this.artworks = artworks;
          this.isReady = true;
        }
      );
  }

  openDialog() {
    this.dialog.open(UploadComponent, {
      data: {
        artist: this.artist,
      }
    });
  }




}
