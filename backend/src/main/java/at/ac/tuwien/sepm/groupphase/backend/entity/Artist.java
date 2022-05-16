package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("Artist")
@Entity
public class Artist extends ApplicationUser {


    @Column(length = 200)
    private String description;

    @Column
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

    @OneToMany(mappedBy = "artist")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Tag> tags;

    public Artist(String userName, String name, String surname, String email, String address, String password,
                  Boolean admin, UserRole userRole, String description, String profileSettings, double reviewScore,
                  Gallery gallery, List<Artwork> artworks, List<Commission> commissions, List<Review> reviews, List<Tag> tags) {
        super(userName, name, surname, email, address, password, admin, userRole);
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
        return "Artist{" +
            "description='" + description + '\'' +
            ", profileSettings='" + profileSettings + '\'' +
            ", reviewScore=" + reviewScore +
            ", gallery=" + gallery.getId() +
            ", artworks=" + artworks.stream().map(Artwork::getId).toList() +
            ", commissions=" + commissions.stream().map(Commission::getId).toList() +
            ", reviews=" + reviews.stream().map(Review::getId).toList() +
            ", tags=" + tags.stream().map(Tag::getName).toList() +
            "} " + super.toString();
    }

    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artist other = (Artist) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }


}