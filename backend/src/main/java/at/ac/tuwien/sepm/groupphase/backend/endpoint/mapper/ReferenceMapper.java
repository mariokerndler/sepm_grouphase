package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReceiptDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReferenceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reference;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class ReferenceMapper {

    public abstract ReferenceDto referenceToReferenceDto(Reference reference);

    public abstract Reference referenceDtoToReference(ReceiptDto receiptDto);

    @AfterMapping
    protected void addCommissionId(Reference reference, @MappingTarget ReferenceDto referenceDto) {
        referenceDto.setCommissionId(reference.getCommission().getId());
    }
}
