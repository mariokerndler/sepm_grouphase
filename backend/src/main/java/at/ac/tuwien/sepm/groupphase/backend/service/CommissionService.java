package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;

public interface CommissionService {

    void saveCommission(Commission c);
    void updateCommission(Commission c);
    void deleteCommission(Commission c);
}
