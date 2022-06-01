import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';
import {ReferenceImageDto} from './referenceImageDto';
import {ArtworkDto} from './artworkDto';

export class CommissionDto {
  id: number;
  artistDto: ArtistDto;
  customerDto: ApplicationUserDto;
  title: string;
  instructions: string;
  sketchesShown: number;
  feedbackSend: number;
  price: number;
  issueDate: string;
  deadlineDate: string;
  referencesDtos: ArtworkDto[];
}
