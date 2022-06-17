export class NotificationDto {
  id?: number;
  title: string;
  createdAt: Date;
  isRead: boolean;
  type: NotificationType;
  referenceId: number;
  userId: number;
}

export enum NotificationType {
  commissionCandidateAdded = 'COMMISSION_CANDIDATE_ADDED',
  commissionCandidateRemoved = 'COMMISSION_CANDIDATE_REMOVED',
  commissionSketchAdded = 'COMMISSION_SKETCH_ADDED',
  commissionFeedbackAdded = 'COMMISSION_FEEDBACK_ADDED',
  commissionStatusCancelled = 'COMMISSION_STATUS_CANCELLED',
  commissionStatusInProgress ='COMMISSION_STATUS_IN_PROGRESS',
  commissionStatusCompleted = 'COMMISSION_STATUS_COMPLETED',
  commissionStatusNegotiating = 'COMMISSION_STATUS_NEGOTIATING'
}
