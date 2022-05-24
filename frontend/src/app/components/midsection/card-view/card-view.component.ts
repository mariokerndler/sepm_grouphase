import {Component, Input, OnInit} from '@angular/core';
import {Artwork} from '../../../dtos/artwork';
import {Globals} from '../../../global/globals';
import {Router} from '@angular/router';






@Component({
  selector: 'app-card-view',
  templateUrl: './card-view.component.html',
  styleUrls: ['./card-view.component.scss']
})
export class CardViewComponent implements OnInit {
  @Input() artwork: Artwork;
  url = 'assets/';


  constructor(private globals: Globals, private router: Router) {

  }

  ngOnInit(): void {
  }



}
