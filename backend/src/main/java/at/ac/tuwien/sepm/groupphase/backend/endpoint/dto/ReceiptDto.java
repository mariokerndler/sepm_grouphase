package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptDto {

    private Long id;
    private double price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;
    private double taxRate;
    private List<String> companyInfo;
    private Long commissionId;

    public ReceiptDto(double price, LocalDateTime issueDate, double taxRate, List<String> companyInfo, Long commissionId) {
        this.price = price;
        this.issueDate = issueDate;
        this.taxRate = taxRate;
        this.companyInfo = companyInfo;
        this.commissionId = commissionId;
    }

    @Override
    public String toString() {
        return "ReceiptDto{"
            + "id=" + id
            + ", price=" + price
            + ", issueDate=" + issueDate
            + ", taxRate=" + taxRate
            + ", companyInfo=" + companyInfo
            + ", commissionId=" + commissionId + '}';
    }
}
