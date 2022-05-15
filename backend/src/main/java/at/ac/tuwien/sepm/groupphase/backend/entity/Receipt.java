package at.ac.tuwien.sepm.groupphase.backend.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Receipt {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false, name = "issue_date")
    private LocalDateTime issueDate;
    @Column(nullable = false, name = "tax_rate")
    private double taxRate;
    //TODO: where is company info stored?? hardcoded? or maybe changeable by admin?
    @Transient
    @Column(nullable = false, name = "company_info")
    private String[] companyInfo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Commission commission;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ApplicationUser applicationUser;


    public Receipt() {
    }

    public Receipt(double price, LocalDateTime issueDate, double taxRate, String[] companyInfo, Commission commission, ApplicationUser applicationUser) {
        this.price = price;
        this.issueDate = issueDate;
        this.taxRate = taxRate;
        this.companyInfo = companyInfo;
        this.commission = commission;
        this.applicationUser = applicationUser;
    }
}
