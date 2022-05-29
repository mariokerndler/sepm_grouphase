package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Review;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReviewRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ReviewRepository reviewRepo;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Override
    public Review findById(Long id) {
        LOGGER.info("Find commission with id " + id);
        Optional<Review> review = reviewRepo.findById(id);

        if (review.isPresent()) {
            return review.get();
        } else {
            throw new NotFoundException(String.format("Could not find review with id %s", id));
        }
    }
}
