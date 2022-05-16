package at.ac.tuwien.sepm.groupphase.backend.entity;


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
public class Receipt {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false, name = "issue_date")
    private LocalDateTime issueDate;
    @Column(nullable = false, name = "tax_rate")
    private double taxRate;

    @ElementCollection
    private List<String> companyInfo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Commission commission;

    public Receipt(double price, LocalDateTime issueDate, double taxRate, List<String> companyInfo, Commission commission) {
        this.price = price;
        this.issueDate = issueDate;
        this.taxRate = taxRate;
        this.companyInfo = companyInfo;
        this.commission = commission;
    }
}
