import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Globals} from '../../../../global/globals';

@Component({
  selector: 'app-review-star',
  templateUrl: './review-star.component.html',
  styleUrls: ['./review-star.component.scss']
})
export class ReviewStarComponent implements OnInit {

  @Input() public disableInteraction = false;
  @Input() public rating = 3;
  @Output() private ratingUpdated = new EventEmitter();
  ratingArr = [];

  constructor(public globals: Globals) {
  }

  ngOnInit(): void {
    for (let index = 0; index < this.globals.maxStarRating; index++) {
      this.ratingArr.push(index);
    }
  }

  onClick(rating: number) {
    if (!this.disableInteraction) {
      this.rating = rating;
      this.ratingUpdated.emit(rating);
    }
  }

  showIcon(index: number) {
    if (this.rating >= index + 1) {
      return 'star';
    } else {
      return 'star_border';
    }
  }
}
