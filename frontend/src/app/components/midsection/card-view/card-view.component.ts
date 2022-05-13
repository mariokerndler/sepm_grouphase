import {Component, Input, OnInit} from '@angular/core';
import {Artwork} from '../../../dtos/artwork';

@Component({
  selector: 'app-card-view',
  templateUrl: './card-view.component.html',
  styleUrls: ['./card-view.component.scss']
})
export class CardViewComponent implements OnInit {

  @Input() artwork: Artwork;

  constructor() { }

  ngOnInit(): void {
  }

}
