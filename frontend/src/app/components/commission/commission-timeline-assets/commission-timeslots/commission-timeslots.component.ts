import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReviewDto} from '../../../../dtos/reviewDto';
import {SketchDto} from '../../../../dtos/sketchDto';

@Component({
  selector: 'app-commission-timeslots',
  templateUrl: './commission-timeslots.component.html',
  styleUrls: ['./commission-timeslots.component.scss']
})
export class CommissionTimeslotsComponent implements OnInit {
  @Input() index: number;
  @Input() sketch: SketchDto;

  @Output() selectArtworkEvent = new EventEmitter();


  constructor() {
  }

  ngOnInit(): void {
  }

  transformIndex(index){
    switch (index){
      case 0: return 'first';
      case 1: return 'second';
      case 2: return 'third';
      case 3: return 'fourth';
      case 4: return 'fifth';
      case 5: return 'sixth';
      case 6: return 'seventh';
      case 7: return 'eighth';
      case 8: return 'ninth';
      default: return 'tenth';
    }
  }

  selectArtwork(){
    this.selectArtworkEvent.emit();
  }

}

