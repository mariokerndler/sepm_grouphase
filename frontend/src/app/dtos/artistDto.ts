import {CommissionDto} from './commissionDto';
import {ReviewDto} from './reviewDto';
import {ApplicationUserDto} from './applicationUserDto';

export class ArtistDto extends ApplicationUserDto {
  reviewScore: number;
  galleryId: number;
  artworkIds: number[];
  commissions: CommissionDto[];
  reviews: ReviewDto[];
  description?: string;
  profileSettings?: string;
}

export enum UserRole {
  user = 'User',
  admin = 'Admin',
  artist = 'Artist',
}
