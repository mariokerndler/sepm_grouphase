package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Review {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser customer;

    @Column(nullable = false, length = 250)
    private String text;

    @OneToOne
    private Commission commission;

    @Column(nullable = false, name = "star_rating")
    private int starRating;

    public Review() {
    }

    public Review(Artist artist, ApplicationUser customer, String text, Commission commission, int starRating) {
        this.artist = artist;
        this.customer = customer;
        this.text = text;
        this.commission = commission;
        this.starRating = starRating;
    }
}
