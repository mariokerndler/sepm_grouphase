package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CommissionMapper {

    public abstract SimpleCommissionDto commissionToCommissionDto(Commission commission);

    public abstract Commission commissionDtoToCommission(SimpleCommissionDto commissionDto);

    //TODO: see what before or after mappings are needed
}
