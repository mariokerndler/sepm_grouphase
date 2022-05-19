import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {Artist} from '../../../dtos/artist';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {MatDialog} from '@angular/material/dialog';
import {ArtistPageEditComponent} from '../artist-page-edit/artist-page-edit.component';

@Component({
  selector: 'app-artist-page',
  templateUrl: './artist-page.component.html',
  styleUrls: ['./artist-page.component.scss']
})
export class ArtistPageComponent implements OnInit, OnDestroy {

  artist: Artist;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fakerService: FakerGeneratorService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.routeSubscription = this.route.params
      .subscribe(params => this.fakerService
        .generateFakeArtist(params.id, params.id + 1, 5)
        .subscribe(artist => this.artist = artist));
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  openEditDialog(): void {
    const dialogRef = this.dialog.open(ArtistPageEditComponent, {
      data: {
        description: this.artist.description
      }
    });

    dialogRef.afterClosed()
      .subscribe(
        (result) => {
          if(result) {
            this.artist.description = result.description;
          }
        });
  }
}
