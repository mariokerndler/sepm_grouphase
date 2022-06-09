import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';
import {ReferenceDto} from './referenceDto';
import {CommissionState} from '../global/CommissionState';

export class CommissionDto {
  id: number;
  artistDto: ArtistDto;
  customerDto: ApplicationUserDto;
  title: string;
  instructions: string;
  sketchesShown: number;
  feedbackSend: number;
  feedbackRounds: number;
  price: number;
  issueDate: string;
  deadlineDate: string;
  referencesDtos: ReferenceDto[];
  commissionState: CommissionState;
}
