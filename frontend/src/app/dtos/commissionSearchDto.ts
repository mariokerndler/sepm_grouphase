import {SearchConstraint} from '../global/SearchConstraint';

export class CommissionSearchDto {
  priceRange: number[];
  dateOrder: SearchConstraint;
  name: string;
  artistId: string;
  userId: string;
  pageNr: number;
}
