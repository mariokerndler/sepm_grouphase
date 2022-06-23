import {ArtistDto} from './artistDto';
import {ApplicationUserDto} from './applicationUserDto';

export class ReviewDto {
  id?: number;
  artistDto: ArtistDto;
  customerDto: ApplicationUserDto;
  text: string;
  commissionId: number;
  starRating: number;
}
