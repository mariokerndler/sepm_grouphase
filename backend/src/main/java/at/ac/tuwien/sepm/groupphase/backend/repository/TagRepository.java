package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM Tag LEFT JOIN ARTWORK_TAG ON ARTWORK_TAG.TAG_ID=Tag.Id WHERE Artwork_TAG.ARTWORK_ID=:artworkId")
    List<Tag> findArtworkTags(@Param("artworkId") Long artworkId);
}