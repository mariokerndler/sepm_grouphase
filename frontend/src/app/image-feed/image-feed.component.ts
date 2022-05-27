import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../services/artwork.service';
import {TagSearch} from '../dtos/tag-search';
import {ArtworkDto} from '../dtos/artworkDto';
import {ArtistDto} from '../dtos/artistDto';
import {FormControl} from '@angular/forms';
import {ArtistService} from '../services/artist.service';
import {map, startWith} from 'rxjs/operators';
import {TagService} from '../services/tag.service';
import {TagDto} from '../dtos/tagDto';
@Component({
  selector: 'app-image-feed',
  templateUrl: './image-feed.component.html',
  styleUrls: ['./image-feed.component.scss']
})


export class ImageFeedComponent implements OnInit {
  @Input()artworks: ArtworkDto[];
  //maybe use as component
  filterArtistId=null;


  //rename Artwork
  url = 'assets/';
  images: ArtworkDto[];
  artists: ArtistDto[];
  tags: TagDto[];
  searchParams: TagSearch={
    pageNr:0,
    randomSeed:1,
    searchOperations :'',
    tagIds:[]
  };
  public  selectedArtwork: number=null;
  constructor(private artworkService: ArtworkService,private artistService: ArtistService,private tagService:TagService) {
    this.artworkService=artworkService;
    this.tagService=tagService;
    this.artistService=artistService;
  }
   ngOnInit(): void {
    this.loadFeed();
    this.loadArtists();
    this.loadAllTags();
  }
  public loadFeed(){
    this.artworkService.search(this.searchParams).subscribe(
      data=>{

        data.forEach(a=>{


        });
        this.images=data;
      },error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }
  private loadArtists(){
    this.artistService.getAllArtists().subscribe(
      data=>{

        data.forEach(a=>{


        });
        this.artists=data;
      },error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }
  private loadAllTags(){
    this.tagService.getAllTags().subscribe(
      data=>{

        data.forEach(a=>{


        });
        this.tags=data;
      },error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  public nextPage():void {
    this.searchParams.pageNr+=1;
    this.loadFeed();
  }

  public previousPage(): void {
    if(this.searchParams.pageNr!==0){
      this.searchParams.pageNr-=1;
      this.loadFeed();
    }

  }
}
