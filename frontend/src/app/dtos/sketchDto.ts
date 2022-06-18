import {FileType} from './artworkDto';

export class SketchDto {
  id: number;
  image: string | ArrayBuffer;
  fileType: FileType.jpg | FileType.png | FileType.gif;
  imageUrl: string;
  description: string;
  customerFeedback: string;
  imageData: number[];
  artworkId: number;
}
