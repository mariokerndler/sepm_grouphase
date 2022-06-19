package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

public final class ArtworkValidationMessages {
    public static final String NAME_SIZE_TOO_LONG = "Name must be shorter than 50 characters.";

    public static final String NAME_ALPHA_NUMERIC_SPACES = "Name must only contain valid alpha numeric characters or spaces.";

    public static final String DESCRIPTION_SIZE_TOO_LONG = "Description must be shorter than 255 characters.";

    public static final String DESCRIPTION_ALPHA_NUMERIC_SPACES = "Description must only contain valid alpha numeric characters or spaces.";

    public static final String IMAGE_URL_SIZE_TOO_LONG = "Image URL must be shorter than 255 characters.";

    public static final String FILE_TYPE_NOT_NULL = "File type cannot be null.";

    public static final String ARTIST_ID_NOT_NULL = "Artist ID cannot be null.";

    public static final String ARTIST_ID_NEGATIVE = "Artist ID cannot be negative.";
}

