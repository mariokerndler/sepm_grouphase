package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;

import java.util.List;

public interface CommissionService {

    /**
     * This method returns all commissions saved in the database.
     *
     * @return a list of all commissions
     */
    List<Commission> getAllCommissions();

    /**
     * This method returns all commissions by the artist with the specified id,
     * if there are commissions with this artist's id saved in the database.
     *
     * @param id: the artist id to look for
     * @return a list of all commissions by the artist with the specified id
     */
    List<Commission> findCommissionsByArtist(Long id);

    /**
     * This method returns the commission with the specified id, if there is a commission with this id saved in the database.
     *
     * @param id: the id to look for
     * @return the commission with the specified id
     */
    Commission findById(Long id);

    /**
     * This method saves the given commission in the database.
     * The entity, as saved in the database, is returned - including the newly generated id.
     *
     * @param commission: the commission being saved
     * @return the saved entity
     */
    void saveCommission(Commission commission);

    /**
     * This method updates the given commission, if there is a commission with this id saved in the database.
     *
     * @param commission: the commission being updated
     */
    void updateCommission(Commission commission);

    /**
     * This method deletes the given commission from the database - including all references, receipts, review and artwork.
     * Artist and customer entities remain saved in the database.
     *
     * @param commission: the commission to be deleted
     */
    void deleteCommission(Commission commission);

}
