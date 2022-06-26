package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommissionSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;

import java.io.IOException;
import java.util.List;

public interface CommissionService {

    /**
     * Returns all commissions saved in the database.
     *
     * @return A list of all commissions
     */
    List<Commission> getAllCommissions();

    /**
     * Returns all commissions by the artist with the specified id,
     * if there are commissions with this artist's id saved in the database.
     *
     * @param id The artist id to look for.
     * @return A list of all commissions by the artist with the specified id.
     */
    List<Commission> findCommissionsByArtist(Long id);

    /**
     * Returns the commission with the specified id, if there is a commission with this id saved in the database.
     *
     * @param id The id to look for.
     * @return The commission with the specified id.
     */
    Commission findById(Long id);

    /**
     * Saves the given commission in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param commission The commission being saved.
     */
    Commission saveCommission(Commission commission) throws IOException;

    /**
     * Updates the given commission, if there is a commission with this id saved in the database.
     *
     * @param commission The commission being updated.
     */
    void updateCommission(Commission commission);

    /**
     * Deletes the given commission from the database - including all references, receipts, review and artwork.
     * Artist and customer entities remain saved in the database.
     *
     * @param commission The commission to be deleted.
     */
    void deleteCommission(Commission commission);

    /**
     * updates the given commission from the database to assign an artist
     * Artist and customer entities saved in the database.
     *
     * @param commission The commission to be deleted.
     */
    void assignArtist(Commission commission);

    /**
     * Returns all commissions by the given parameters (lower Price range, upper Price Range, date order, name, artist),
     * if there are commissions saved in the database.
     *
     * @param cs CommissionSearchDto (lower Price range, upper Price Range, date order, name, artist).
     * @return A list of all commissions by the artist with the specified id.
     */
    List<Commission> searchCommissions(CommissionSearchDto cs);
}
