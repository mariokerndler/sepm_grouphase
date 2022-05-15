package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Artist")
public class Artist extends ApplicationUser {


    @Column
    private double reviewScore;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @OneToMany(mappedBy = "artist")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Artwork> artworks;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Commission> commissions;
    @OneToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Review> reviews;
    @Column
    private String artistSettings;

    public Artist() {

    }

    public Artist(String userName, String name, String surname, String email, String address,
                  String password, Boolean admin, UserRole userRole,
                  double reviewScore, Gallery gallery, List<Artwork> artworks,
                  List<Commission> commissions, List<Review> reviews, String artistSettings) {

        super(userName, name, surname, email, address, password, admin, userRole);
        this.reviewScore = reviewScore;
        this.gallery = gallery;
        this.artworks = artworks;
        this.commissions = commissions;
        this.reviews = reviews;
        this.artistSettings = artistSettings;
    }

}