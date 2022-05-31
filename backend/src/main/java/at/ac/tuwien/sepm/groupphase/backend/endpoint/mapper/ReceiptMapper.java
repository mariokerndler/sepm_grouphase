package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReceiptDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Receipt;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public abstract class ReceiptMapper {

    public abstract ReceiptDto receiptToReceiptDto(Receipt receipt);

    public abstract Receipt receiptDtoToReceipt(ReceiptDto receiptDto);

    @AfterMapping
    protected void addCommissionId(Receipt receipt, @MappingTarget ReceiptDto receiptDto) {
        receiptDto.setCommissionId(receipt.getCommission().getId());
    }
}