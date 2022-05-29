package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;


@Service
public class CommissionServiceImpl implements CommissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CommissionRepository commissionRepo;
    private final ImageFileManager ifm;

    @Autowired
    public CommissionServiceImpl(CommissionRepository commissionRepo, ImageFileManager ifm) {
        this.commissionRepo = commissionRepo;
        this.ifm = ifm;
    }

    @Override
    public List<Commission> getAllCommissions() {
        LOGGER.info("Get all commissions");
        return commissionRepo.findAll();
    }

    @Override
    public Commission findById(Long id) {
        LOGGER.info("Find commission with id " + id);
        Optional<Commission> commission = commissionRepo.findById(id);

        if (commission.isPresent()) {
            return commission.get();
        } else {
            throw new NotFoundException(String.format("Could not find Commission with id %s", id));
        }
    }

    @Override
    public void saveCommission(Commission c) {
        LOGGER.info("Save commission " + c);
        //TODO: writeReference in imageFileManager
        /*
        if (c.getReferences() != null) {
            List<Reference> references = c.getReferences();
            for (Reference r : references) {
                //r.setImageUrl(this.ifm.writeReference(r));
            }
        }
         */
        this.commissionRepo.save(c);
    }

    @Override
    public void updateCommission(Commission c) {
        LOGGER.info("Update commission " + c);
        this.commissionRepo.save(c);
    }

    @Override
    public void deleteCommission(Commission c) {
        LOGGER.info("Delete commission " + c);
        //TODO: i want to delete the entire commission here (admin functionality) - including reference pictures, sketches and the finished artwork
        if (c.getArtwork() != null) {
            this.ifm.deleteArtistImage(c.getArtwork());
        }

        //TODO: deleteReference in imageFileManager
        /*if (c.getReferences() != null) {
            List<Reference> references = c.getReferences();
            for (Reference r : references) {

                //this.ifm.deleteReference(r);
            }
        }*/
        this.commissionRepo.deleteById(c.getId());
    }
}
