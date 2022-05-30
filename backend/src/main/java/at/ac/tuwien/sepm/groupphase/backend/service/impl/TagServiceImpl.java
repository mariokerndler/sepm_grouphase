package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.repository.TagRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepo;

    @Autowired
    public TagServiceImpl(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public List<Tag> loadAllTags() {
        return tagRepo.findAll();
    }

    @Override
    public List<Tag> loadTagsByImage(Long id) {
        return this.tagRepo.findArtworkTags(id);
    }
}
