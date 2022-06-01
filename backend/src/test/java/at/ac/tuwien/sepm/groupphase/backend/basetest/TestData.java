package at.ac.tuwien.sepm.groupphase.backend.basetest;

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

    /**
     * imageUrl0: "https://i.ibb.co/HTT7Ym3/image0.jpg"
     * imageUrl1: "https://i.ibb.co/7yHp276/image1.jpg"
     * imageUrl2: "https://i.ibb.co/cDT8JHg/image2.jpg"
     * imageUrl3: "https://i.ibb.co/wy4PbD4/image3.jpg"
     *
     * imageSketch1Url: "https://i.ibb.co/jfQR7W9/sketch1.jpg"
     * imageSketch2Url: "https://i.ibb.co/JRcbTDk/sketch2.jpg"
     * imageSketch3Url: "https://i.ibb.co/pdtMdcJ/sketch3.jpg"
     * imageSketch4Url: "https://i.ibb.co/09Fk1PB/sketch4.jpg"
     * imageSketchGifUrl: "https://i.ibb.co/pf63fMd/sketch.gif"
     */

}

