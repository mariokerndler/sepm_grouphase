import {Component, EventEmitter, HostListener, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {ArtworkDto} from '../../dtos/artworkDto';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {ArtistService} from '../../services/artist.service';
import {ArtistDto} from '../../dtos/artistDto';
import {Router} from '@angular/router';
import {TagService} from '../../services/tag.service';
import {GlobalFunctions} from '../../global/globalFunctions';

@Component({
  encapsulation: ViewEncapsulation.None,
  selector: 'app-gallery-carousel',
  templateUrl: './gallery-carousel.component.html',
  styleUrls: ['./gallery-carousel.component.scss'],
  animations: [
    trigger('slide-in', [
      state('left', style({
        opacity: 0,
        transform: 'translateX(+100%)'
      })),
      state('middle', style({
        opacity: 1,
        transform: 'translateX(0)'
      })),
      state('right', style({
        opacity: 0,
        transform: 'translateX(-100%)'
      })),
      transition('left => middle', [
        style({
          opacity: 0,
          transform: 'translateX(-100%)'
        }),
        animate('200ms')
      ]),
      transition('right => middle', [
        style({
          opacity: 0,
          transform: 'translateX(+100%)'
        }),
        animate('200ms')
      ]),
      transition('middle => *', [
        animate('200ms')
      ])
    ])
  ]
})
export class GalleryCarouselComponent implements OnInit {
  @Input() artworks: ArtworkDto[];
  @Input() selectedArtworkId: number;
  @Output() closeCarousel = new EventEmitter<void>();
  public animState = 'middle';
  public artist: ArtistDto = {
    address: '',
    admin: false,
    artworkIds: [],
    commissions: [],
    email: '',
    galleryId: 0,
    name: '',
    password: '',
    reviewScore: 0,
    reviews: [],
    surname: '',
    userName: '',
    userRole: undefined

  };
  public animArtwork: number;
  url = 'assets/';
  public imageTags = [];
  private artistService: ArtistService;
  private router: Router;

  constructor(artistService: ArtistService, router: Router, public tagService: TagService,
              public globalFunctions: GlobalFunctions) {
    this.globalFunctions = globalFunctions;
    this.tagService = tagService;
    this.router = router;
    this.artistService = artistService;
  }

  @HostListener('document:keydown.escape', ['$event']) onKeydownHandler(evt: KeyboardEvent) {
    this.close();
  }

  ngOnInit(): void {
    this.animArtwork = this.selectedArtworkId;
    //console.log(this.selectedArtworkId);
    this.getImageArtistInfo();
  }

  public onEvent(event: Event): void {
    event.stopPropagation();
  }

  public close(): void {
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(0px)');
    this.closeCarousel.emit();
  }

  public previous(): void {
    this.getImageArtistInfo();
    this.animState = 'left';
    this.selectedArtworkId = this.selectedArtworkId > 0 ? this.selectedArtworkId - 1 : this.artworks.length - 1;

    this.blur();
  }

  public next(): void {
    this.getImageArtistInfo();
    this.animState = 'right';
    this.selectedArtworkId = this.selectedArtworkId > this.artworks.length - 2 ? 0 : this.selectedArtworkId + 1;

  }

  public animDone(): void {
    this.animArtwork = this.selectedArtworkId;
    this.animState = 'middle';
  }

  public blur(): void {
    const activeElem = document.activeElement as HTMLElement;
    if (activeElem !== null) {
      activeElem.blur();
    }
  }

  public loadImageTags() {
    //console.log('id '+ this.selectedArtworkId);
    this.tagService.getImageTags(this.artworks[this.selectedArtworkId].id).subscribe(
      data => {
        //console.log(data);
        this.imageTags = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  public getImageArtistInfo(): void {
    this.loadImageTags();
    this.artistService.getArtistById(this.artworks[this.selectedArtworkId].artistId).subscribe(
      data => {
        this.artist = data;
      }
    );
  }

  public routeToArtist(): void {
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(0px)');
    this.router.navigate(['/artist/' + this.artworks[this.selectedArtworkId].artistId.toString()]);

  }

  public stringifyTags(): string {
    let result = '';
    this.imageTags.forEach(
      t => {
        result += t.name + ' \n';
      }
    );
    return result.substring(0, result.length - 1);
  }
}