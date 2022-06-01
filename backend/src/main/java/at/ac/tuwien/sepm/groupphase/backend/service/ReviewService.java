package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Review;

public interface ReviewService {

    /**
     * Returns the review with the specified id, if there is a review with this id saved in the database.
     *
     * @param id The id to look for.
     * @return The review with the specified id.
     */
    Review findById(Long id);
}
