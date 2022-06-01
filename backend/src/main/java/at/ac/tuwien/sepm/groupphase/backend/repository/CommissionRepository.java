package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List<Commission> findCommissionsByArtist(Long id);
}