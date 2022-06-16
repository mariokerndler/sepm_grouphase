import {FileType} from './artworkDto';

export class ProfilePictureDto {
  id?: number;
  imageUrl?: string;
  fileType: FileType;
  imageData: number[];
  applicationUser?: number;
}
