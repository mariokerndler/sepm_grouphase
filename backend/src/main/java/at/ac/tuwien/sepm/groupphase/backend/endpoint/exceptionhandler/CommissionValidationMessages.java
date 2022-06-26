package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

public final class CommissionValidationMessages {
    public static final String CUSTOMER_IS_NULL = "Customer cannot be null.";

    public static final String COMMISSION_STATUS_IS_NULL = "Commission status cannot be null.";

    public static final String SKETCHES_SHOWN_NEGATIVE = "Shown sketches cannot negative";

    public static final String FEEDBACK_SENT_NEGATIVE = "Feedback sent cannot be negative.";

    public static final String PRICE_NEGATIVE = "Price cannot be negative.";

    public static final String FEEDBACK_ROUNDS_NEGATIVE = "Feedback rounds cannot be negative.";

    public static final String ISSUE_DATE_NOT_PAST_PRESENT = "Issue date cannot be in the future.";

    public static final String DEADLINE_DATE_NOT_FUTURE = "Deadline date cannot be in the past/presence.";

    public static final String TILE_LENGTH_TOO_LONG = "Title must be shorter than 50 characters.";

    public static final String TITLE_NON_ALPHA_NUMERIC_SPACES = "Title must only contain valid alpha numeric characters and spaces.";

    public static final String INSTRUCTIONS_TOO_LONG = "Instructions must be shorter than 255 characters.";
}
