package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long>, JpaSpecificationExecutor<Artwork> {
    List<Artwork> findArtworkByArtistId(Long id);


    @Query(nativeQuery = true, value = "SELECT *  FROM artwork where RIGHT(artwork.IMAGE_URL, 3)='jpg' or RIGHT(artwork.IMAGE_URL, 3)='png' or RIGHT(artwork.IMAGE_URL, 3)='gif' ORDER BY random()")
    Slice<Artwork> findArtworkRandom(@Param("seed") int seed, Pageable pageable);
}
