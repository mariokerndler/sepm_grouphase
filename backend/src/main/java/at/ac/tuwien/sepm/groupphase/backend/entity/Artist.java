package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Artist")
public class Artist extends ApplicationUser  {


    @Column
    private double reviewScore;
    @Column
    private long galleryId;
    @OneToMany(mappedBy = "artist")
    private List<Artwork> artworks;
    @ElementCollection
    private List<String> commissions;
    @ElementCollection
    private List<String> reviews;

    public Artist() {

    }

    public Artist(String userName, String name, String surname, String email, String address,
                  String password, Boolean admin, UserRole userRole,
                  double reviewScore, long galleryId, List<Artwork> artworks,
                  List<String> commissions, List<String> reviews) {

        super(userName, name, surname, email, address, password, admin, userRole);
        this.reviewScore = reviewScore;
        this.galleryId = galleryId;
        this.artworks = artworks;
        this.commissions = commissions;
        this.reviews = reviews;
    }

}