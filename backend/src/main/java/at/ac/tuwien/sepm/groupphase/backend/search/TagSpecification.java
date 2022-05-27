package at.ac.tuwien.sepm.groupphase.backend.search;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TagSpecification {
    public static Specification<Artwork> filterByTags(String tagId){
        return (artwork, query, criteriaBuilder) ->
            criteriaBuilder.equal(artwork.join("tags").get("id"),tagId);
    }




}
