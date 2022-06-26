import {CommissionStatus} from '../global/CommissionStatus';

export class SimpleCommissionDto {
  id: number;
  artistId: number;
  customerId: number;
  title: string;
  instructions: string;
  sketchesShown: number;
  feedbackSend: number;
  price: number;
  issueDate: string;
  deadlineDate: string;
  status: CommissionStatus;
  feedbackRounds: number;
  referenceImageIds: number[];
}
