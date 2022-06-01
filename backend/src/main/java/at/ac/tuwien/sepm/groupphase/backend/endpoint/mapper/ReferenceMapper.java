package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReferenceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reference;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CommissionService.class})
public abstract class ReferenceMapper {

    @Mapping(source = "commission.id", target = "commissionId")
    public abstract ReferenceDto referenceToReferenceDto(Reference reference);

    @Mapping(source = "commissionId", target = "commission")
    public abstract Reference referenceDtoToReference(ReferenceDto referenceDto);

    @Named("ReferenceWithoutCommissionId")
    public abstract Reference referenceDtoWithoutCommissionIdToReference(ReferenceDto referenceDto);

}
