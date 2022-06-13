package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Artist")
@Entity
public class Artist extends ApplicationUser implements HasId {

    @Column
    private String description;

    @Column(length = 500)
    private String profileSettings;

    @Column
    private double reviewScore;

    @OneToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;

    @OneToMany(mappedBy = "artist")
    private List<Artwork> artworks;

    @OneToMany(mappedBy = "artist")
    private List<Commission> commissions;

    @OneToMany(mappedBy = "artist")
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
        name = "artist_tag",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"artist_id", "tag_id"})})
    private List<Tag> tags;


    public Artist(String userName,
                  ProfilePicture profilePicture,
                  String name,
                  String surname,
                  String email,
                  String address,
                  String password,
                  Boolean admin,
                  UserRole userRole,
                  String description,
                  String profileSettings,
                  double reviewScore,
                  Gallery gallery,
                  List<Artwork> artworks,
                  List<Commission> commissions,
                  List<Review> reviews,
                  List<Tag> tags) {
        super(userName, profilePicture, name, surname, email, address, password, admin, userRole);
        this.description = description;
        this.profileSettings = profileSettings;
        this.reviewScore = reviewScore;
        this.gallery = gallery;
        this.artworks = artworks;
        this.commissions = commissions;
        this.reviews = reviews;
        this.tags = tags;
    }

    public Artist(Long id,
                  String userName,
                  ProfilePicture profilePicture,
                  String name,
                  String surname,
                  String email,
                  String address,
                  String password,
                  Boolean admin,
                  UserRole userRole,
                  String description,
                  String profileSettings,
                  double reviewScore,
                  Gallery gallery,
                  List<Artwork> artworks,
                  List<Commission> commissions,
                  List<Review> reviews,
                  List<Tag> tags) {
        super(userName, profilePicture, name, surname, email, address, password, admin, userRole);
        this.setId(id);
        this.description = description;
        this.profileSettings = profileSettings;
        this.reviewScore = reviewScore;
        this.gallery = gallery;
        this.artworks = artworks;
        this.commissions = commissions;
        this.reviews = reviews;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Artist{"
            + ", description='" + description + '\''
            + ", profileSettings='" + profileSettings + '\''
            + ", reviewScore=" + reviewScore
            + ", gallery=" + (gallery == null ? null : gallery.getId())
            + ", artworks=" + (artworks == null ? null : artworks.stream().map(Artwork::getId))
            + ", commissions=" + (commissions == null ? null : commissions.stream().map(Commission::getId))
            + ", reviews=" + (reviews == null ? null : reviews.stream().map(Review::getId))
            + ", tags=" + tags + "} " + super.toString();
    }

    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        Artist other = (Artist) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }


}