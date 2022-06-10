import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-commission-timeslots',
  templateUrl: './commission-timeslots.component.html',
  styleUrls: ['./commission-timeslots.component.scss']
})
export class CommissionTimeslotsComponent implements OnInit {
  @Input() index: number;
  @Input() items;

  constructor() { }

  ngOnInit(): void {
  }

}
