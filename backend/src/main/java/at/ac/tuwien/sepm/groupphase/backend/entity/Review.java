package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser customer;

    //TODO: why length gone??
    @Column(nullable = false)
    private String text;

    @OneToOne
    @JoinColumn(nullable = false)
    private Commission commission;

    @Column(nullable = false, name = "star_rating")
    private int starRating;

    public Review(Artist artist, ApplicationUser customer, String text, Commission commission, int starRating) {
        this.artist = artist;
        this.customer = customer;
        this.text = text;
        this.commission = commission;
        this.starRating = starRating;
    }

    @Override
    public String toString() {
        return "Review{"
            + "id=" + id
            + ", artist=" + artist.getId()
            + ", customer=" + customer.getId()
            + ", text='" + text + '\''
            + ", commission=" + commission.getId()
            + ", starRating=" + starRating
            + '}';
    }

    @Override
    public int hashCode() {
        return 29;
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
        Review other = (Review) obj;
        return id != null && id.equals(other.getId());
    }

}
