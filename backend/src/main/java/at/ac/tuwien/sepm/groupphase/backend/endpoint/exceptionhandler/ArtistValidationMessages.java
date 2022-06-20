package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

public final class ArtistValidationMessages {
    public static final String DESCRIPTION_SIZE_TOO_BIG = "Description size should be less than 255 characters.";

    public static final String REVIEW_SCORE_TOO_LOW = "Review score should be bigger than 0";
    public static final String REVIEW_SCORE_TOO_HIGH = "Review score should be less than or equal 5";

    public static final String PROFILE_SETTINGS_TOO_BIG = "Profile settings should be less than 255 characters.";
}
