package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CommissionMapper {

    public abstract CommissionDto commissionToCommissionDto(Commission commission);

    public abstract Commission commissionDtoToCommission(CommissionDto commissionDto);

    //TODO: see what before or after mappings are needed
}
