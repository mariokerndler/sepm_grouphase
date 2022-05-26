package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;

import java.util.List;

public interface CommissionService {

    List<Commission> getAllCommissions();

    Commission findById(Long id);

    void saveCommission(Commission c);

    void updateCommission(Commission c);

    void deleteCommission(Commission c);

}
