import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';
import * as data from 'src/assets/commission2.json';

@Component({
  selector: 'app-commission-timeline',
  templateUrl: './commission-timeline.component.html',
  styleUrls: ['./commission-timeline.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CommissionTimelineComponent implements OnInit {
  data;
  constructor() { }

  ngOnInit(): void {
    this.data = data;
    console.log(this.data);
  }

}
