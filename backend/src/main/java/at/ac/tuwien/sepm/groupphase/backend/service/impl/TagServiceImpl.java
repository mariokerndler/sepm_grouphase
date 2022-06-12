package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.repository.TagRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepo;

    @Autowired
    public TagServiceImpl(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public List<Tag> loadAllTags() {
        log.trace("calling loadAllTags() ...");
        List<Tag> foundTags = tagRepo.findAll();
        log.info("Retrieved all tags ({}).", foundTags.size());
        return foundTags;
    }

    @Override
    public List<Tag> loadTagsOfArtwork(Long id) {
        log.trace("calling loadTagsOfArtwork() ...");
        List<Tag> foundTags = this.tagRepo.findArtworkTags(id);
        log.info("Retrieved all tags for artwork with id='{}' ({})", id, foundTags.size());
        return foundTags;
    }
}
