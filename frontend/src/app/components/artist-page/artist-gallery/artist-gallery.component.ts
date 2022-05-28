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


  constructor(private artworkService: ArtworkService) {}


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
          this.artistProfilePicture = e.target.result;
        };
      };
      reader.readAsDataURL(file.target.files[0]);
      reader.onload = () => {
        const base64result = reader.result.toString().split(',')[1];
        const dataType = ((reader.result.toString().split(',')[0]).split(';')[0]).split('/')[1];
        let filetype = FileType.jpg;
        if(dataType === 'png'){
          filetype = FileType.png;
        }
        if(dataType === 'gif'){
          filetype = FileType.gif;
        }
        console.log(filetype);
        const binary = new Uint8Array(this.base64ToBinaryArray(base64result));
        const image = Array.from(binary);
        console.log(image);
        this.uploadNewImage(image, filetype);

      };
    }
  }

  uploadNewImage(imageData, filetype){
    const artwork = {name:'test10', description:'test2', imageData,
      imageUrl:'', fileType: filetype, artistId:this.artist.id} as ArtworkDto;
    this.artworkService.createArtwork(artwork).subscribe();
  }



    base64ToBinaryArray(base64: string) {

    const binary = window.atob(base64);
    const length = binary.length;
    const bytes = new Uint8Array(length);
    for (let i = 0; i < length; i++) {
      bytes[i] = binary.charCodeAt(i);
    }
    return bytes.buffer;
  }




}
