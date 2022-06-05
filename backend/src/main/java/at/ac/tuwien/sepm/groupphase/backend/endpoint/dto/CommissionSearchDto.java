package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommissionSearchDto {

    private  String priceAsc;
    private  String priceDesc;
    private  String priceUpper;
    private  String priceLower;
    private  String date;
    private  String name;
}
