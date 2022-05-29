package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query(nativeQuery=true, value="select * from TAG left Join ARTWORK_TAG on Tag.ID = Artwork_Tag.tag_ID WHERE  ARtwork_ID=:aId")
    List<Tag> findArtworkTags(@Param("aId") Long aId);
}