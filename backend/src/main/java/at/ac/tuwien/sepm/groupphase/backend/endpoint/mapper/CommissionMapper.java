package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reference;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ArtistMapper.class, ReferenceMapper.class, ReceiptMapper.class, ReviewMapper.class, ArtworkMapper.class, HasIdMapper.class})
public abstract class CommissionMapper {

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "references", target = "referencesIds")
    public abstract SimpleCommissionDto commissionToSimpleCommissionDto(Commission commission);

    @Mapping(source = "artistId", target = "artist", qualifiedByName = "idToArtist")
    @Mapping(source = "customerId", target = "customer", qualifiedByName = "idToUser")
    public abstract Commission simpleCommissionDtoToCommission(SimpleCommissionDto simpleCommissionDto);

    @Mapping(source = "artist", target = "artistDto")
    @Mapping(source = "artistCandidates", target = "artistCandidatesDtos")
    @Mapping(source = "customer", target = "customerDto")
    @Mapping(source = "references", target = "referencesDtos")
    @Mapping(source = "receipts", target = "receiptsDtos")
    @Mapping(source = "review", target = "reviewDto")
    @Mapping(source = "artwork", target = "artworkDto")
    public abstract DetailedCommissionDto commissionToDetailedCommissionDto(Commission commission);

    @Mapping(source = "artistDto", target = "artist")
    @Mapping(source = "artistCandidatesDtos", target = "artistCandidates")
    @Mapping(source = "customerDto", target = "customer")
    @Mapping(source = "referencesDtos", target = "references", qualifiedByName = "ReferenceWithoutCommissionId")
    @Mapping(source = "receiptsDtos", target = "receipts")
    @Mapping(source = "reviewDto", target = "review")
    @Mapping(source = "artworkDto", target = "artwork")
    public abstract Commission detailedCommissionDtoToCommission(DetailedCommissionDto detailedCommissionDto);

    @AfterMapping
    public Commission setCommissionForChildren(@MappingTarget Commission.CommissionBuilder commissionBuilder) {
        Commission commission = commissionBuilder.build();
        if (commission.getReferences() != null) {
            for (Reference r : commission.getReferences()) {
                r.setCommission(commission);
            }
        }
        return commission;
    }

}
