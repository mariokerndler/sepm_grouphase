import {Component, Input, OnInit} from '@angular/core';
import {ArtworkDto} from '../../../dtos/artworkDto';

@Component({
  selector: 'app-card-view',
  templateUrl: './card-view.component.html',
  styleUrls: ['./card-view.component.scss']
})
export class CardViewComponent implements OnInit {
  @Input() artwork: ArtworkDto;
  url = 'assets/';

  constructor() {}

  ngOnInit(): void {
  }
}
