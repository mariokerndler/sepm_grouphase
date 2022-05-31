import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';

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
  referenceImageIds: number[];
}
