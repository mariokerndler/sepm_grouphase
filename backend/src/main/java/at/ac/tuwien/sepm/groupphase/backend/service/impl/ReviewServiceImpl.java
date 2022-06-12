package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReviewRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepo;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Review findById(Long id) {
        log.trace("calling findById() ...");
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isPresent()) {
            log.info("Retrieved review with id='{}'", id);
            return review.get();
        } else {
            throw new NotFoundException(String.format("Could not find review with id %s", id));
        }
    }
}
