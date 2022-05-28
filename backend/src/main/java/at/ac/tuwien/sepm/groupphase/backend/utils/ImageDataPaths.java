package at.ac.tuwien.sepm.groupphase.backend.utils;

import java.nio.file.Path;

public class ImageDataPaths {
    public static final String tagLocation = "data/tags/tags.txt";
    public static final String artistProfileLocation = "data/ap/";
    public static final String artistProfilePictureLocation = "data/app/";
    public static final String artistProfilePictureIdentifier = "profile";


    public static final String assetAbsoluteLocation = Path.of("").toAbsolutePath().toString().replace("\\backend", "") + "\\frontend\\src\\assets\\";

}
