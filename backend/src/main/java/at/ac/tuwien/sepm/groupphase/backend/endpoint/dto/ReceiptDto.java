package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptDto {

    private Long id;
    private double price;
    private LocalDateTime issueDate;
    private double taxRate;
    private List<String> companyInfo;
    private Long commissionId;

    @Override
    public String toString() {
        return "ReceiptDto{" +
            "id=" + id +
            ", price=" + price +
            ", issueDate=" + issueDate +
            ", taxRate=" + taxRate +
            ", companyInfo=" + companyInfo +
            ", commissionId=" + commissionId +
            '}';
    }
}
