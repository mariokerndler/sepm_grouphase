export class Commission {
  id: number;
  artistId: number;
  userId: number;
  sketchesShown: number;
  feedbackSend: number;
  comArtworkId: number;
  feedback: string[];
  price: number;
  instructions: string;
  startDate: Date;
  endDate: Date;
  referenceImageIds: number[];
}
