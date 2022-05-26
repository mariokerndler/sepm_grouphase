package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ArtistDto {

    private Long id;

    private String userName;

    private String name;

    private String surname;

    private String email;

    private String address;

    private String password;

    private Boolean admin;

    private UserRole userRole;

    private double reviewScore;

    private long galleryId;

    private List<Long> artworksIds;

    private List<Commission> commissions;

    private List<Review> reviews;

    private String artistSettings;

    public ArtistDto(String userName, String name, String surname, String email, String address,
                     String password, Boolean admin, UserRole userRole, double reviewScore, long galleryId,
                     List<Long> artworksIds, List<Commission> commissions, List<Review> reviews, String artistSettings) {
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.password = password;
        this.admin = admin;
        this.userRole = userRole;
        this.reviewScore = reviewScore;
        this.galleryId = galleryId;
        this.artworksIds = artworksIds;
        this.commissions = commissions;
        this.reviews = reviews;
        this.artistSettings = artistSettings;

    }
    public  void addArtworkId(Long i){
        if(artworksIds==null){
            return;
        }
        this.artworksIds.add(i);
    }
}