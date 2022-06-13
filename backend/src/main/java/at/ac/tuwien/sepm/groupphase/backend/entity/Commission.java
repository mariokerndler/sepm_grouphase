package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Commission implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Artist artist;

    @ManyToMany
    private List<Artist> artistCandidates;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser customer;

    @Column(nullable = false)
    private CommissionStatus status;

    @Column(nullable = false, name = "sketches_shown")
    private int sketchesShown;

    @Column(nullable = false, name = "feedback_sent")
    private int feedbackSent;

    @Column(nullable = false, name = "feedback_rounds")
    private int feedbackRounds;

    @Column(nullable = false)
    private double price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, name = "issue_date")
    private LocalDateTime issueDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column (name = "deadline_date")
    private LocalDateTime deadlineDate;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 512)
    private String instructions;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<Reference> references;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<Receipt> receipts;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private Review review;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private Artwork artwork;

    public Commission(Artist artist,
                      List<Artist> artistCandidates,
                      ApplicationUser customer,
                      CommissionStatus status,
                      int sketchesShown,
                      int feedbackSent,
                      double price,
                      LocalDateTime issueDate,
                      LocalDateTime deadlineDate,
                      String title,
                      String instructions,
                      List<Reference> references,
                      List<Receipt> receipts,
                      Review review,
                      Artwork artwork) {
        this.artist = artist;
        this.artistCandidates = artistCandidates;
        this.customer = customer;
        this.status = status;
        this.sketchesShown = sketchesShown;
        this.feedbackSent = feedbackSent;
        this.price = price;
        this.issueDate = issueDate;
        this.deadlineDate = deadlineDate;
        this.title = title;
        this.instructions = instructions;
        this.references = references;
        this.receipts = receipts;
        this.review = review;
        this.artwork = artwork;
    }

    @Override
    public String toString() {
        return "Commission{"
            + "id=" + id
            + ", artistId=" + (artist == null ? null : artist.getId())
            + ", artistCandidatesIds=" + (artistCandidates == null ? null : artistCandidates.stream().map(Artist::getId).toList())
            + ", customerId=" + customer.getId()
            + ", status=" + status
            + ", sketchesShown=" + sketchesShown
            + ", feedbackSent=" + feedbackSent
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", deadlineDate=" + deadlineDate
            + ", title=" + title
            + ", instructions='" + instructions + '\''
            + ", referencesIds=" + (references == null ? null : references.stream().map(Reference::getId).toList())
            + ", receiptsIds=" + (receipts == null ? null : receipts.stream().map(Receipt::getId).toList())
            + ", review=" + review
            + ", artworkId=" + (artwork == null ? null : artwork.getId()) + '}';
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
