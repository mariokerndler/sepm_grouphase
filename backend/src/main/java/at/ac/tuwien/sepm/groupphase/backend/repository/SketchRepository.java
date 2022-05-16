package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sketch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SketchRepository extends JpaRepository<Sketch, Long> {
}