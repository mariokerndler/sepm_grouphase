export class CommissionDto {
  id: number;
  artistId: number;
  userId: number;
  title: string;
  description: string;
  sketchesShown: number;
  feedbackSend: number;
  comArtworkId: number;
  feedback: string[];
  price: number;
  startDate: Date;
  endDate: Date;
  referenceImageIds: number[];
}
