import {Component, Input, OnInit} from '@angular/core';
import {Artist} from '../../../dtos/artist';

@Component({
  selector: 'app-artist-information',
  templateUrl: './artist-information.component.html',
  styleUrls: ['./artist-information.component.scss']
})
export class ArtistInformationComponent implements OnInit {

  @Input() artist: Artist;
  artistUrl = 'https://picsum.photos/150/150';
  tabIndex = 0;

  constructor() {}

  ngOnInit(): void {}

  switchTab(index: number) {
    this.tabIndex = index;
  }
}
