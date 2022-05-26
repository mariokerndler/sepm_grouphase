import {Component, Input, OnInit} from '@angular/core';
import {ArtistDto} from '../../../dtos/artistDto';

@Component({
  selector: 'app-artist-information',
  templateUrl: './artist-information.component.html',
  styleUrls: ['./artist-information.component.scss']
})
export class ArtistInformationComponent implements OnInit {

  @Input() artist: ArtistDto;
  // TODO: Fill in the real profile picture
  artistUrl = 'https://picsum.photos/150/150';
  tabIndex = 0;

  constructor() {}

  ngOnInit(): void {}

  switchTab(index: number) {
    this.tabIndex = index;
  }
}
