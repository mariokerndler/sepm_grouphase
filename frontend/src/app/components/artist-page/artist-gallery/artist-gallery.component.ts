import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../../../services/artwork.service';
import {UploadComponent} from '../../upload/upload.component';
import {MatDialog} from '@angular/material/dialog';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-artist-gallery',
  templateUrl: './artist-gallery.component.html',
  styleUrls: ['./artist-gallery.component.scss']
})
export class ArtistGalleryComponent implements OnInit {

  @Input() artist;
  artworks: ArtworkDto[] = [];
  isReady = false;

  private authId: number;

  constructor(
    private artworkService: ArtworkService,
    public dialog: MatDialog,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.artworkService.getArtworksByArtist(this.artist.id)
      .subscribe(
        (artworks) => {
          this.artworks = artworks;
          this.isReady = true;
        }
      );

    this.authId = this.authService.getUserId();
  }

  openDialog() {
    const dialogRef = this.dialog.open(UploadComponent, {
      data: {
        artist: this.artist,
      }
    });
    dialogRef.afterClosed().subscribe(
       result => {
         if(result.event === 'upload') {
           this.switchTab();
         }
       }
    );
  }

  isSameUser(): boolean {
    return this.authId === this.artist.id;
  }

  switchTab() {
    sessionStorage.setItem('reloading', 'true');
    document.location.reload();
  }
}
