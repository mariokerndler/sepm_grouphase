import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';
import {ArtworkDto} from './artworkDto';
import {ReferenceDto} from './referenceDto';

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
  referencesDtos: ReferenceDto[];
}
