package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Review;

public interface ReviewService {

    Review findById(Long id);
}
