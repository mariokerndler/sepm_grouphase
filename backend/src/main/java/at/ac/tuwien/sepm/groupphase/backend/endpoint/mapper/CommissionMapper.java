package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ArtistMapper.class, ArtistService.class, UserService.class})
public abstract class CommissionMapper {

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "customer.id", target = "customerId")
    public abstract SimpleCommissionDto commissionToSimpleCommissionDto(Commission commission);

    @Mapping(source = "artistId", target = "artist")
    //@Mapping(source = "customerId", target = "customer")
    public abstract Commission simpleCommissionDtoToCommission(SimpleCommissionDto simpleCommissionDto);

    @Mapping(source = "artist", target = "artistDto")
    @Mapping(source = "customer", target = "customerDto")
    public abstract DetailedCommissionDto commissionToDetailedCommissionDto(Commission commission);

    @Mapping(source = "artistDto", target = "artist")
    @Mapping(source = "customerDto", target = "customer")
    public abstract Commission detailedCommissionDtoToCommission(DetailedCommissionDto detailedCommissionDto);

}
