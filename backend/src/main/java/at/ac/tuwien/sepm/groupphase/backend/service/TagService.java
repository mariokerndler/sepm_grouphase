package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;

import java.util.List;

public interface TagService {

    /**
     * Returns all tags saved in the database.
     *
     * @return A list of all tags.
     */
    List<Tag> loadAllTags();

    /**
     * Returns a list of all tags of the artwork with the specified id.
     *
     * @return A list of all tags of the artwork with the specified id.
     */
    List<Tag> loadTagsOfArtwork(Long id);
}
