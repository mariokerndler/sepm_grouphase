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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + id +
            ", price=" + price +
            ", issueDate=" + issueDate +
            ", taxRate=" + taxRate +
            ", companyInfo=" + companyInfo +
            ", commission=" + commission.getId() +
            '}';
    }

    @Override
    public int hashCode() {
        return 23;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Receipt other = (Receipt) obj;
        return id != null && id.equals(other.getId());
    }

}
