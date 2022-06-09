package at.ac.tuwien.sepm.groupphase.backend.utils;

import java.nio.file.Path;

public class ImageDataPaths {
    public static final String tagLocation = "data\\tags\\tags.txt";
    public static final String artistProfileLocation = "data\\ap\\";
    public static final String userProfilePictureLocation = "data\\upp\\";
    public static final String userProfilePictureIdentifier = "profile";
    public static final String refIdentifier = "ref";
    public static final String sketchIdentifier = "sk";
    public static final String awhIdentifier = "aw";
    public static final String commissionLocation = "data\\com\\";


    public static final String assetAbsoluteLocation = Path.of("").toAbsolutePath().toString().replace("\\backend", "") + "\\frontend\\src\\assets\\";

}
