package at.ac.tuwien.sepm.groupphase.backend.search;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.jpa.domain.Specification;

public class ArtistSpecification {
    public static Specification<Artwork> filterBy(String artistId) {
        return (artwork, query, criteriaBuilder) ->
            criteriaBuilder.equal(artwork.join("artist").get("id"), artistId);
    }
}
