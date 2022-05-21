package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> loadAllTags();
}
