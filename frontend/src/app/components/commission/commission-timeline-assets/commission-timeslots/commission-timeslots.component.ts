import {Component, Input, OnInit} from '@angular/core';
import {ReviewDto} from '../../../../dtos/reviewDto';
import {SketchDto} from '../../../../dtos/sketchDto';

@Component({
  selector: 'app-commission-timeslots',
  templateUrl: './commission-timeslots.component.html',
  styleUrls: ['./commission-timeslots.component.scss']
})
export class CommissionTimeslotsComponent implements OnInit {
  @Input() index: number;
  @Input() review: ReviewDto;
  @Input() sketch: SketchDto;

  constructor() {
  }

  ngOnInit(): void {
  }

}
