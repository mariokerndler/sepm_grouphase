import {Component, EventEmitter, Input, OnInit, Output, ViewEncapsulation} from '@angular/core';
import {ArtistDto} from '../../../dtos/artistDto';
import {ArtistProfileSettings} from '../artist-page-edit/artistProfileSettings';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {ChatDto} from '../../../dtos/chatDto';
import {ChatService} from '../../../services/chat-service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-artist-information',
  templateUrl: './artist-information.component.html',
  styleUrls: ['./artist-information.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ArtistInformationComponent implements OnInit {

  @Input() artist: ArtistDto;
  @Output() tabIndexEvent = new EventEmitter<number>();
  profileSettings: ArtistProfileSettings;
  artworks: ArtworkDto[];
  isReady = false;
  loggedInUserId;
  public selectedArtwork: number = null;

  constructor(
    private artworkService: ArtworkService,
    public globalFunctions: GlobalFunctions,
    private chatService: ChatService,
    private router: Router) {
  }

  ngOnInit(): void {
    if (this.artist.profileSettings) {
      this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
      console.log(this.profileSettings);
    }
    this.loggedInUserId = Number.parseInt(localStorage.getItem('userId'), 10);
  }


  switchTab(index) {
    this.tabIndexEvent.emit(index);
  }

  messageUser() {
    const chat: ChatDto = {
      chatPartnerId: this.artist.id, userId: this.loggedInUserId
    };
    console.log(chat);
    this.chatService.postChat(chat).subscribe(success => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    }, error => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    });
  }
}
