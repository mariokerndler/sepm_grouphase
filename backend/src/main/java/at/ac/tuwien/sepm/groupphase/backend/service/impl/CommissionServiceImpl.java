package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommissionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CommissionServiceImpl implements CommissionService {

    private final CommissionRepository commissionRepo;
    private  final ImageFileManager ifm;

    @Autowired
    public CommissionServiceImpl(CommissionRepository commissionRepo, ImageFileManager ifm) {
        this.commissionRepo = commissionRepo;
        this.ifm = ifm;
    }

    //TODO: what about exceptions?
    @Override
    public void saveCommission(Commission c) {

        //TODO: CODING CONVENTIONS PLS edit this
        log.info(c.toString());
        this.commissionRepo.save(c);
    }

    @Override
    public void updateCommission(Commission c) {

        //TODO: CODING CONVENTIONS PLS edit this
        log.info(c.toString());
        this.commissionRepo.save(c);
    }

    @Override
    public void deleteCommission(Commission c) {
        this.commissionRepo.deleteById(c.getId());
        //TODO: also delete images and references and stuff
        //this.ifm.deleteArtistImage(c);
    }
}
