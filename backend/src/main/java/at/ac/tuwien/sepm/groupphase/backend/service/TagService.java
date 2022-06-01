package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;

import java.util.List;

public interface TagService {

    /**
     * This method returns all tags saved in the database.
     *
     * @return a list of all tags
     */
    List<Tag> loadAllTags();

    /**
     * This method returns a list of all tags of the artwork with the specified id.
     *
     * @return a list of all tags of the artwork with the specified id
     */
    List<Tag> loadTagsOfArtwork(Long id);
}
