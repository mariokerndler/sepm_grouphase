import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtworkDto, FileType} from '../../../dtos/artworkDto';

@Component({
  selector: 'app-artist-gallery',
  templateUrl: './artist-gallery.component.html',
  styleUrls: ['./artist-gallery.component.scss']
})
export class ArtistGalleryComponent implements OnInit {

  @Input() artist;
  artistProfilePicture: string;


  constructor(private artworkService: ArtworkService) {
  }


  ngOnInit(): void {
    this.artistProfilePicture = 'https://picsum.photos/100/100';
  }

  onFileChanged(file: any) {
    if (file.target.files && file.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = new Image();
        image.src = e.target.result;
        image.onload = (_) => {
          const imageData = new Uint8Array([0xff, 0xc0, 0xff, 0xc0, 0xff, 0xc0, 0xff, 0xc0, 0xff, 0xc0, 0xf3, 0xc0, 0xff, 0xc0, 0xff, 0xc0,
            0xf7, 0xc0, 0xff, 0xc0]);
          const artwork = {
            name: 'test', description: 'test', imageData, imageUrl: '/data/ap/aaronjoshuaaa/test.jpg',
            fileType: FileType.jpg, artistId: this.artist.id
          } as ArtworkDto;
          this.artworkService.createArtwork(artwork).subscribe();
        };
      };
      reader.readAsDataURL(file.target.files[0]);
    }
  }
}
