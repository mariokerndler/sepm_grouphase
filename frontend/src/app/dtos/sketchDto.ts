import {FileType} from './artworkDto';

export class SketchDto {
  id: number;
  image: number[];
  fileType: FileType.jpg | FileType.png | FileType.gif;
  imageUrl: string;
  description: string;
  customerFeedback: string;
  imageData: string | ArrayBuffer;
  artworkId: number;
}
