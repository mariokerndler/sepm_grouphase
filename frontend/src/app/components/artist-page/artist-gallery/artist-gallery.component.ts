import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../../../services/artwork.service';
import {UploadComponent} from '../../upload/upload.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-artist-gallery',
  templateUrl: './artist-gallery.component.html',
  styleUrls: ['./artist-gallery.component.scss']
})
export class ArtistGalleryComponent implements OnInit {

  @Input() artist;
  artistProfilePicture: string;


  constructor(private artworkService: ArtworkService, public dialog: MatDialog) {}


  ngOnInit(): void {
    this.artistProfilePicture = 'https://picsum.photos/100/100';
  }

  openDialog() {
    this.dialog.open(UploadComponent, {
      data: {
        artist: this.artist,
      }
    });
  }




}
