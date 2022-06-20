package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;

public final class ApplicationUserValidationMessages {
    public static final String USERNAME_NO_VALID_ALPHA_NUMERIC = "Username should only contain alphanumeric characters.";
    public static final String USERNAME_SIZE_TOO_BIG = "Username must be shorter than " + ApplicationUserDto.NAME_SIZE + " characters.";

    public static final String NAME_NO_VALID_ALPHA_NUMERIC_WITH_SPACES = "Name should only contain alphanumeric characters and spaces.";
    public static final String NAME_SIZE_TOO_BIG = "Name must be shorter than " + ApplicationUserDto.NAME_SIZE + " characters.";

    public static final String SURNAME_NO_VALID_ALPHA_NUMERIC_WITH_SPACES = "Surname should only contain alphanumeric characters and spaces.";
    public static final String SURNAME_SIZE_TOO_BIG = "Surname must be shorter than " + ApplicationUserDto.NAME_SIZE + " characters.";

    public static final String EMAIL_NOT_VALID = "E-mail must be in a valid format.";
    public static final String EMAIL_SIZE_TOO_BIG = "E-mail must be shorter than " + ApplicationUserDto.NAME_SIZE + " characters.";
    public static final String EMAIL_NOT_NULL = "E-mail can't be null.";

    public static final String ADDRESS_NOT_BLANK = "Address can't be blank or null.";
    public static final String ADDRESS_SIZE_TOO_BIG = "Address must be shorter than " + ApplicationUserDto.ADDRESS_PASSWORD_SIZE + " characters.";

    public static final String PASSWORD_NOT_BLANK = "Password can't be blank or null.";
    public static final String PASSWORD_SIZE_TOO_BIG = "Password must be shorter than " + ApplicationUserDto.ADDRESS_PASSWORD_SIZE + " characters.";

    public static final String ADMIN_NOT_NULL = "Admin can't be null.";

    public static final String USER_ROLE_NOT_NULL = "User role can't be null.";
}
