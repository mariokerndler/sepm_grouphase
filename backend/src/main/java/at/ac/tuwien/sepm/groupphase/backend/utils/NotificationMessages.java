package at.ac.tuwien.sepm.groupphase.backend.utils;

public final class NotificationMessages {

    // Commission candidate added
    public static final String COMMISSION_CANDIDATE_ADDED_TITLE = "A new candidate has been added to your commission.";
    public static final String COMMISSION_CANDIDATE_ADDED_MESSAGE = "I HAVE NO IDEA WHAT TO WRITE HERE!";

    // Commission candidate removed
    public static final String COMMISSION_CANDIDATE_REMOVED_TITLE = "You have been removed as a candidate from a commission.";
    public static final String COMMISSION_CANDIDATE_REMOVED_MESSAGE = "I HAVE NO IDEA WHAT TO WRITE HERE!";

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

    public static final String COMMISSION_STATUS_CANCELLED_TITLE = "The commission has been cancelled.";
    public static final String COMMISSION_STATUS_COMPLETED_TITLE = "The commission has been completed.";
}
