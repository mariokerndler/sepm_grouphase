package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/commissions")
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
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all commissions")
    @Transactional
    public List<SimpleCommissionDto> getAllCommissions() {
        LOGGER.info("Get all commissions");
        return commissionService.getAllCommissions().stream().map(u -> commissionMapper.commissionToSimpleCommissionDto(u)).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get commission by id")
    public DetailedCommissionDto findById(@PathVariable Long id) {
        LOGGER.info("Get commission with id " + id);
        return commissionMapper.commissionToDetailedCommissionDto(commissionService.findById(id));
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Post commission")
    public void postCommission(@RequestBody DetailedCommissionDto commissionDto) {
        LOGGER.info("Post commission " + commissionDto);
        try {
            commissionService.saveCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update commission")
    @Transactional
    public void updateCommission(@RequestBody DetailedCommissionDto commissionDto) {
        LOGGER.info("Update commission " + commissionDto);
        try {
            commissionService.updateCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + commissionDto);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    //TODO: permit only admin
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @Operation(summary = "Delete commission")
    public void deleteCommission(@RequestBody DetailedCommissionDto commissionDto) {
        LOGGER.info("Delete commission " + commissionDto);
        try {
            commissionService.deleteCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
}
