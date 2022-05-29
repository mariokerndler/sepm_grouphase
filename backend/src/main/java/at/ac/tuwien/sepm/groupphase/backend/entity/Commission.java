package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Commission implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser customer;

    @Column(nullable = false, name = "sketches_shown")
    private int sketchesShown;

    @Column(nullable = false, name = "feedback_sent")
    private int feedbackSent;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false, name = "issue_date")
    private LocalDateTime issueDate;

    @Column(nullable = false, name = "deadline_date")
    private LocalDateTime deadlineDate;

    @Column(nullable = false)
    private String instructions;

    @OneToMany(mappedBy = "commission")
    private List<Reference> references;

    @OneToMany(mappedBy = "commission")
    private List<Receipt> receipts;

    @OneToOne(mappedBy = "commission")
    private Review review;

    @OneToOne(mappedBy = "commission")
    private Artwork artwork;

    public Commission(
        Artist artist,
        ApplicationUser customer,
        int sketchesShown,
        int feedbackSent,
        double price,
        LocalDateTime issueDate,
        LocalDateTime deadlineDate,
        String instructions,
        List<Reference> references,
        List<Receipt> receipts,
        Review review,
        Artwork artwork) {
        this.artist = artist;
        this.customer = customer;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.instructions = instructions;
        this.references = references;
        this.receipts = receipts;
        this.review = review;
        this.artwork = artwork;
    }

    @Override
    public String toString() {
        return "Commission{"
            + "id=" + id + ", artist=" + artist.getId()
            + ", customer=" + customer.getId()
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", deadlineDate=" + deadlineDate
            + ", instructions='" + instructions + '\''
            + ", receipts=" + receipts.stream().map(Receipt::getId).toList()
            + ", review=" + review.getId()
            + ", artwork=" + artwork.getId()
            + '}';
    }

    @Override
    public int hashCode() {
        return 17;
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
        Commission other = (Commission) obj;
        return id != null && id.equals(other.getId());
    }

}
