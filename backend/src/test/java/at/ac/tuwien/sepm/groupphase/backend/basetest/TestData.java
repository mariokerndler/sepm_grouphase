package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.NotificationType;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    // Test Data User
    String TEST_USER_USERNAME = "aUser";
    String TEST_USER_NAME = "aName";
    String TEST_USER_SURNAME = "aSurname";
    String TEST_USER_EMAIL = "user@email.com";
    String TEST_USER_ADDRESS = "Address 1";
    String TEST_USER_PASSWORD = "PASSWORD_HASH";
    Boolean TEST_USER_ADMIN = false;
    UserRole TEST_USER_ROLE = UserRole.User;

    // Test Data User
    String TEST_ARTIST_USERNAME = "anArtist";
    String TEST_ARTIST_NAME = "anArtistName";
    String TEST_ARTIST_SURNAME = "anArtistSurname";
    String TEST_ARTIST_EMAIL = "artist@email.com";
    String TEST_ARTIST_ADDRESS = "Address 1";
    String TEST_ARTIST_PASSWORD = "PASSWORD_HASH";
    Boolean TEST_ARTIST_ADMIN = false;
    UserRole TEST_ARTIST_ROLE = UserRole.Artist;
    String TEST_ARTIST_DESCRIPTION = "Description";
    double TEST_ARTIST_REVIEW_SCORE = 1.0;

    // Test Data Commission
    CommissionStatus TEST_COMMISSION_STATUS = CommissionStatus.LISTED;
    int TEST_COMMISSION_SKETCHES_SHOWN = 3;
    int TEST_COMMISSION_FEEDBACK_SENT = 0;
    double TEST_COMMISSION_PRICE = 300;
    LocalDateTime TEST_COMMISSION_ISSUE_DATE =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);
    LocalDateTime TEST_COMMISSION_DEADLINE_DATE = TEST_COMMISSION_ISSUE_DATE.plusDays(10L);
    String TEST_COMMISSION_TITLE = "A title for a commission";
    String TEST_COMMISSION_INSTRUCTIONS = "Do the thing";

    // Test Data Notification
    String TEST_NOTIFICATION_TITLE = "Test Notification";
    LocalDateTime TEST_NOTIFICATION_CREATED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);
    Boolean TEST_NOTIFICATION_IS_READ = false;
    NotificationType TEST_NOTIFICATION_TYPE = NotificationType.COMMISSION_CANDIDATE_ADDED;
    Long TEST_NOTIFICATION_REFERENCE_ID = 1L;

    String[] IMAGE_URLS = {
        "https://i.ibb.co/HTT7Ym3/image0.jpg",
        "https://i.ibb.co/7yHp276/image1.jpg",
        "https://i.ibb.co/cDT8JHg/image2.jpg",
        "https://i.ibb.co/wy4PbD4/image3.jpg"
    };

    String[] IMAGE_SKETCH_URLS = {
        "https://i.ibb.co/jfQR7W9/sketch1.jpg",
        "https://i.ibb.co/JRcbTDk/sketch2.jpg",
        "https://i.ibb.co/pdtMdcJ/sketch3.jpg",
        "https://i.ibb.co/09Fk1PB/sketch4.jpg"
    };

    String IMAGE_SKETCH_GIF_URL = "https://i.ibb.co/pf63fMd/sketch.gif";

}

