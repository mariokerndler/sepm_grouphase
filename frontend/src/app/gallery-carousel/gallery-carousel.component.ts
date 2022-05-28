import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ArtworkDto} from '../dtos/artworkDto';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {ArtistService} from '../services/artist.service';
import {ArtistDto} from '../dtos/artistDto';
import {Router} from '@angular/router';

@Component({
  selector: 'app-gallery-carousel',
  templateUrl: './gallery-carousel.component.html',
  styleUrls: ['./gallery-carousel.component.scss'],
  animations:[
    trigger('slide-in',[
    state('left',style({
      opacity:0,
      transform:'translateX(+100%)'
    })),
      state('middle',style({
        opacity:1,
        transform:'translateX(0)'
      })),
      state('right',style({
        opacity:0,
        transform:'translateX(-100%)'
      })),
      transition('left => middle',[
        style({
          opacity:0,
          transform:'translateX(-100%)'
        }),
        animate('200ms')
      ]),
      transition('right => middle',[
        style({
          opacity:0,
          transform:'translateX(+100%)'
        }),
        animate('200ms')
      ]),
      transition('middle => *',[
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
  public artist: ArtistDto;
  public animArtwork: number;
  url = 'assets/';
  private artistService: ArtistService;
  private router: Router;

  constructor(artistService: ArtistService, router: Router) {
    this.router =router;
    this.artistService=artistService;
  }

  ngOnInit(): void {
    this.animArtwork = this.selectedArtworkId;
    console.log(this.selectedArtworkId);
    this.getImageArtistInfo();
  }
  public  onEvent(event: Event): void{
    event.stopPropagation();
  }

  public close(): void {
    this.closeCarousel.emit();
  }
  public previous(): void {
    this.getImageArtistInfo();
    this.animState='left';
    this.selectedArtworkId=this.selectedArtworkId>0? this.selectedArtworkId-1 : this.artworks.length-1;

    this.blur();
  }
  public  next(): void{
    this.getImageArtistInfo();
    this.animState='right';
    this.selectedArtworkId=this.selectedArtworkId>this.artworks.length-1 ?0: this.selectedArtworkId+1   ;

  }
public  animDone(): void{
    this.animArtwork= this.selectedArtworkId;
    this.animState='middle';
}
  public blur(): void{
    const activeElem= document.activeElement as HTMLElement;
    if(activeElem!==null){
      activeElem.blur();
    }
  }
  public  getImageArtistInfo(): void{
    this.artistService.getArtistById(this.artworks[this.selectedArtworkId].artistId).subscribe(
      data=>{
        this.artist=data;
      }
    );
  }

  public routeToArtist(): void {
    this.router.navigate(['/artist/'+this.artworks[this.selectedArtworkId].artistId.toString()]);

  }
}
