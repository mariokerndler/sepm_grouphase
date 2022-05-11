import {User} from './user';

export class Artist extends User {
  artworkIds: number[];
  description?: string;
  reviewScore: number;
  galleryId: number;
  commissionIds: number[];
  reviewIds: number[];
}
