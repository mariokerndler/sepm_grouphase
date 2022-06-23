import {Component, Input, OnInit} from '@angular/core';
import {ReviewDto} from '../../../../dtos/reviewDto';

@Component({
  selector: 'app-review-card',
  templateUrl: './review-card.component.html',
  styleUrls: ['./review-card.component.scss']
})
export class ReviewCardComponent implements OnInit {

  @Input() review: ReviewDto;

  constructor() { }

  ngOnInit(): void {
  }

}
