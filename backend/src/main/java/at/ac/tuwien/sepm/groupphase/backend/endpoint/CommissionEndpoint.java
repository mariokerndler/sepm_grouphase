package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;

public class CommissionEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CommissionService commissionService;
    private final CommissionMapper commissionMapper;

    @Autowired
    public CommissionEndpoint(CommissionService commissionService, CommissionMapper commissionMapper) {
        this.commissionService = commissionService;
        this.commissionMapper = commissionMapper;
    }

    //TODO: get all commissions or search commissions like in artworkEndpoint, getAll by not entering criteria

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Post commission")
    public void postCommission(@RequestBody CommissionDto commissionDto) {
        LOGGER.info("Post commission " + commissionDto.toString());
        try {
            commissionService.saveCommission(commissionMapper.commissionDtoToCommission(commissionDto));
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }

    //TODO: is this needed?
/*
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping()
    @Operation(summary = "Delete commission")
    public void deleteCommission(@RequestBody CommissionDto commissionDto){
        LOGGER.info("Delete commission " + commissionDto.toString());
        try {
            commissionService.deleteCommission(commissionMapper.commissionDtoToCommission(commissionDto));
        } catch (Exception n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
 */
}
