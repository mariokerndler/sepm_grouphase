package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommissionSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reference;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SketchRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.NotificationService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.SearchConstraint;
import at.ac.tuwien.sepm.groupphase.backend.utils.validators.CommissionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CommissionServiceImpl implements CommissionService {
    private final CommissionRepository commissionRepo;
    private final SketchRepository sketchRepository;
    private final ArtworkRepository artworkRepository;
    private final CommissionValidator commissionValidator;
    private final ArtworkService artworkService;
    private final ImageFileManager ifm;
    private final NotificationService notificationService;

    @Autowired
    public CommissionServiceImpl(
        CommissionRepository commissionRepo,
        ArtworkRepository artworkRepository, CommissionValidator commissionValidator,
        ImageFileManager ifm,
        ArtworkService artworkService,
        SketchRepository sketchRepository,
        NotificationService notificationService) {
        this.commissionRepo = commissionRepo;
        this.artworkRepository = artworkRepository;
        this.sketchRepository = sketchRepository;
        this.commissionValidator = commissionValidator;
        this.ifm = ifm;
        this.artworkService = artworkService;
        this.notificationService = notificationService;
    }

    @Override
    public List<Commission> getAllCommissions() {
        log.trace("calling getAllCommissions() ...");
        List<Commission> foundCommissions = commissionRepo.findAll();
        log.info("Retrieved all commissions ({})", foundCommissions.size());
        return foundCommissions;
    }

    @Override
    public Commission findById(Long id) {
        log.trace("calling findById() ...");
        Optional<Commission> commission = commissionRepo.findById(id);

        if (commission.isPresent()) {
            log.info("Retrieved commission with id='{}'", id);
            return commission.get();
        } else {
            throw new NotFoundException(String.format("Could not find Commission with id %s", id));
        }
    }

    @Override
    public void saveCommission(Commission c) throws IOException {
        log.trace("calling saveCommission() ...");
        commissionValidator.throwExceptionIfCommissionAlreadyExists(c);

        this.ifm.createCommission(c);
        if (c.getReferences() != null) {
            List<Reference> references = c.getReferences();
            for (Reference r : references) {
                r.setImageUrl(this.ifm.writeReferenceImage(c, r));
            }
        }
        this.commissionRepo.save(c);
        log.info("Saved commission with id='{}'", c.getId());
    }

    @Override
    public void updateCommission(Commission c) {
        log.trace("calling updateCommission() ...");

        notificationService.createNotificationByCommission(findById(c.getId()), c);
        commissionValidator.throwExceptionIfCommissionDoesNotExist(c);

        if(this.commissionRepo.getById(c.getId()).getStatus()== CommissionStatus.LISTED && c.getStatus()==CommissionStatus.NEGOTIATING){
            this.assignArtist(c);
        }
       else  if (c.getArtwork() != null && c.getArtwork().getSketches() != null) {
            int sketchCount = c.getArtwork().getSketches().size();
            //if sketch has been added
            if (c.getFeedbackSent() < c.getSketchesShown()) {
                log.info("Writing Sketch" + c.getFeedbackSent() + " " + c.getSketchesShown());
                c.getArtwork().getSketches().get(sketchCount - 1).setImageUrl(
                    this.ifm.writeSketchImage(c, c.getArtwork().getSketches().get(sketchCount - 1)));
                this.sketchRepository.save(c.getArtwork().getSketches().get(sketchCount - 1));
            }
        } else {
            log.info("SKETCHES EMPTY");
        }
        commissionRepo.save(c);
        log.info("Updated commission with id='{}'", c.getId());
    }

    @Override
    public void deleteCommission(Commission c) {
        log.trace("calling deleteCommission() ...");

        if (c.getArtwork() != null) {
            this.ifm.deleteArtistImage(c.getArtwork());
        }
        this.ifm.deleteCommission(c);

        this.commissionRepo.deleteById(c.getId());
        log.info("Deleted commission with id='{}'", c.getId());
    }

    //This NEEDS to be called AFTER and artist has been assigned to the commission and initializes the artwork
    @Override
    public void assignArtist(Commission commission) {

        Artwork a = new Artwork();
        a.setName(commission.getTitle() + "_Artwork");
        a.setArtist(commission.getArtist());
        a.setFileType(FileType.PNG);
        a.setCommission(commission);
        a.setDescription("Commisson for"+ commission.getCustomer().getUserName());
        a.setImageUrl(this.ifm.writeCommissionArtwork(commission, a));
        //this has to be done through repo cause it doesnt contain image data;
        artworkRepository.save(a);
        commissionRepo.save(commission);


    }

    @Override
    public List<Commission> searchCommissions(CommissionSearchDto cs) {
        log.trace("calling searchCommissions() ...");
        List<Commission> foundCommissions;
        if (cs.getSearchConstraint() == SearchConstraint.None) {
            foundCommissions = this.commissionRepo.searchCommissions(
                cs.getName(),
                cs.getPriceRangeLower(),
                cs.getPriceRangeUpper(),
                cs.getArtistId(),
                cs.getUserId());
        } else {
            foundCommissions = this.commissionRepo.searchCommissionsDate(
                cs.getName(),
                cs.getPriceRangeLower(),
                cs.getPriceRangeUpper(),
                cs.getArtistId(),
                cs.getUserId(),
                cs.getSearchConstraint().toString());
        }

        log.info("Retrieved all commissions {} ({}).",
            !cs.isEmpty() ? ("matching the search request: " + cs) : "",
            foundCommissions.size());
        return foundCommissions;
    }

    @Override
    public List<Commission> findCommissionsByArtist(Long id) {
        log.trace("calling searchCommissions() ...");
        List<Commission> foundCommissions = commissionRepo.findCommissionsByArtist(id);
        log.info("Retrieved all commissions from artist with id='{}'", id);
        return foundCommissions;
    }
}
