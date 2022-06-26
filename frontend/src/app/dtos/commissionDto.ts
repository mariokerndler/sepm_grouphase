import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';
import {ReferenceDto} from './referenceDto';
import {CommissionStatus} from '../global/CommissionStatus';
import {ArtworkDto} from './artworkDto';
import {ReviewDto} from './reviewDto';

export class CommissionDto {
  id: number;
  artistDto: ArtistDto;
  artworkDto: ArtworkDto;
  customerDto: ApplicationUserDto;
  title: string;
  instructions: string;
  sketchesShown: number;
  feedbackSent: number;
  feedbackRounds: number;
  price: number;
  issueDate: string;
  deadlineDate: string;
  artistCandidatesDtos: ArtistDto[];
  referencesDtos: ReferenceDto[];
  status: CommissionStatus;
  reviewDto?: ReviewDto;
}
