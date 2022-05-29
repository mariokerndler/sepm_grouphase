package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.utils.HasId;
import org.mapstruct.Mapper;

@Mapper
public class HasIdMapper {
    final Long toId(HasId entity) {
        return entity.getId();
    }
}