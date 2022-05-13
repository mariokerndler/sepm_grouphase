package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Commission {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

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

    //TODO: do we need a repo for reference pics?
    /*
    @OneToMany(mappedBy = "reference")
    private List<Reference> reference;
     */

    @OneToMany(mappedBy = "commission")
    private List<Receipt> receipts;

    @OneToOne(mappedBy = "commission")
    private Review review;

    /*
    @OneToMany(mappedBy = "sketch")
    private List<Sketch> sketches;
     */



    @OneToOne
    private Artwork artwork;



}
