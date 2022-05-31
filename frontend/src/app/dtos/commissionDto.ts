export class CommissionDto {
  id: number;
  artistId: number;
  customerId: number;
  title: string;
  instructions: string;
  sketchesShown: number;
  feedbackSend: number;
  price: number;
  issueDate: Date;
  deadlineDate: Date;
  referenceImageIds: number[];
}
