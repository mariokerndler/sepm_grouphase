package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;

import java.io.IOException;

public interface CommissionService {

    void saveCommission(Commission c) throws IOException;
    //void deleteCommission(Commission c);
}
