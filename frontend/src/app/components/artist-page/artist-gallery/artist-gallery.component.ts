import {Component, Input, OnInit} from '@angular/core';
import {Artist} from '../../../dtos/artist';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {Gallery} from '../../../dtos/gallery';

@Component({
  selector: 'app-artist-gallery',
  templateUrl: './artist-gallery.component.html',
  styleUrls: ['./artist-gallery.component.scss']
})
export class ArtistGalleryComponent implements OnInit {

  @Input() artist;
  artistProfilePicture: string;


  constructor() {}


  ngOnInit(): void {
    this.artistProfilePicture = 'https://picsum.photos/100/100';
  }

}
