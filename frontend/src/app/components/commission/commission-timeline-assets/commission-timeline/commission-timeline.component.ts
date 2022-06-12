import {ChangeDetectionStrategy, Component, OnInit, ViewEncapsulation} from '@angular/core';
import * as data from 'src/assets/commission.json';

@Component({
  selector: 'app-commission-timeline',
  templateUrl: './commission-timeline.component.html',
  styleUrls: ['./commission-timeline.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CommissionTimelineComponent implements OnInit {
  items = Array.from({length: 8}).map((_, i) => `Item #${i}`);
  data;
  constructor() { }

  ngOnInit(): void {
    this.data = data;
  }

}
