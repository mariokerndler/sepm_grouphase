package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ReviewDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class, UserMapper.class, CommissionService.class})
public abstract class ReviewMapper {

    @Mapping(source = "artist", target = "artistDto")
    @Mapping(source = "customer", target = "customerDto")
    @Mapping(source = "commission.id", target = "commissionId")
    public abstract ReviewDto reviewToReviewDto(Review review);

    @Mapping(source = "artistDto", target = "artist")
    @Mapping(source = "customerDto", target = "customer")
    @Mapping(source = "commissionId", target = "commission")
    public abstract Review reviewDtoToReview(ReviewDto reviewDto);

}
