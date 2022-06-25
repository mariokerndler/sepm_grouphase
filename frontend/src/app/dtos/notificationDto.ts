export class NotificationDto {
  id?: number;
  title: string;
  createdAt: string;
  read: boolean;
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
  commissionStatusInProgress = 'COMMISSION_STATUS_IN_PROGRESS',
  commissionStatusCompleted = 'COMMISSION_STATUS_COMPLETED',
  commissionStatusNegotiating = 'COMMISSION_STATUS_NEGOTIATING',
  commissionCandidateChosen = 'COMMISSION_CANDIDATE_CHOSEN',
  commissionReviewAdded = 'COMMISSION_REVIEW_ADDED',
  reportOffensiveImages = 'REPORT_OFFENSIVE_IMAGES',
  reportStrongLanguage = 'REPORT_STRONG_LANGUAGE',
  reportRacistRemarks = 'REPORT_RACIST_REMARKS',
  reportIllegalArt = 'REPORT_ILLEGAL_ART',
  reportStolenArt = 'REPORT_STOLEN_ART'
}
