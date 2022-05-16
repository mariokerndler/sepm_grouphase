import {Component, Input, OnInit} from '@angular/core';
import {Artist} from '../../../dtos/artist';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

@Component({
  selector: 'app-artist-information',
  templateUrl: './artist-information.component.html',
  styleUrls: ['./artist-information.component.scss']
})
export class ArtistInformationComponent implements OnInit {

  @Input() artist: Artist;

  artistProfilePicture: string;

  constructor() { }

  ngOnInit(): void {
    this.artistProfilePicture = 'https://picsum.photos/100/100';
  }
}
