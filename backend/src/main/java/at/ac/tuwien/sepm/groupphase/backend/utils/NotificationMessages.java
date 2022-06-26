package at.ac.tuwien.sepm.groupphase.backend.utils;

public final class NotificationMessages {

    // Commission candidate added
    public static final String COMMISSION_CANDIDATE_ADDED_TITLE = "A new candidate has been added to your commission.";

    // Commission candidate removed
    public static final String COMMISSION_CANDIDATE_REMOVED_TITLE = "You have been removed as a candidate from a commission.";

    // Commission sketch added
    public static String commissionSketchAddedTitle(int amount) {
        if (amount == 1) {
            return "A new sketch has been added to your commission.";
        } else {
            return amount + " new sketches have been added to your commission.";
        }
    }

    // Commission feedback added
    public static String commissionFeedbackAddedTitle(int amount) {
        if (amount == 1) {
            return "A new feedback has been added to your commission.";
        } else {
            return amount + " new feedback have been added to your commission.";
        }
    }

    // Commission status changed
    public static final String COMMISSION_STATUS_CANCELLED_TITLE = "The commission has been cancelled.";
    public static final String COMMISSION_STATUS_COMPLETED_TITLE = "The commission has been completed.";
    public static final String COMMISSION_STATUS_IN_PROGRESS_TITLE = "The commission is now in progress.";
    public static final String COMMISSION_STATUS_NEGOTIATING = "The commission information has been edited.";

    // Candidate has been chosen
    public static final String COMMISSION_CANDIDATE_CHOSEN = "You have been chosen for the commission.";

    // Review has been uploaded
    public static final String COMMISSION_REVIEW_UPLOADED = "You have received a review for your work.";
}
