package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommissionSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleCommissionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommissionMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.SearchConstraint;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RestController
@RequestMapping("api/v1/commissions")
public class CommissionEndpoint {
    private final CommissionService commissionService;
    private final CommissionMapper commissionMapper;

    @Autowired
    public CommissionEndpoint(CommissionService commissionService, CommissionMapper commissionMapper) {
        this.commissionService = commissionService;
        this.commissionMapper = commissionMapper;
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all commissions")
    @Transactional
    public List<SimpleCommissionDto> getAllCommissions() {
        log.info("A users is fetching all commissions.");
        return commissionService.getAllCommissions().stream()
            .map(commissionMapper::commissionToSimpleCommissionDto).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    @Operation(summary = "Get all commissions filtered")
    @Transactional
    public List<SimpleCommissionDto> searchCommissions(@RequestParam(defaultValue = "", name = "name") String name,
                                                       @RequestParam(defaultValue = "0", name = "pageNr") String pageNr,
                                                       @RequestParam(defaultValue = "50000", name = "priceRangeUpper") String priceRangeUpper,
                                                       @RequestParam(defaultValue = "0", name = "priceRangeLower") String priceRangeLower,
                                                       @RequestParam(defaultValue = "", name = "artistId") String artistId,
                                                       @RequestParam(defaultValue = "", name = "userId") String userId,
                                                       @RequestParam(defaultValue = "None", name = "date") String dateConstraint
    ) {
        log.info("A user is trying to search for a commission.");
        CommissionSearchDto cs = new CommissionSearchDto();
        cs.setName(name.toLowerCase());
        cs.setArtistId(artistId);

        if (dateConstraint.toLowerCase().contains(SearchConstraint.ASC.toString().toLowerCase())) {
            cs.setSearchConstraint(SearchConstraint.ASC);
        } else if (dateConstraint.toLowerCase().contains(SearchConstraint.DESC.toString().toLowerCase())) {
            cs.setSearchConstraint(SearchConstraint.DESC);
        } else {
            cs.setSearchConstraint(SearchConstraint.None);
        }

        cs.setPriceRangeLower(priceRangeLower);
        cs.setPriceRangeUpper(priceRangeUpper);
        cs.setPageNr(pageNr);
        cs.setUserId(userId);

        return commissionService.searchCommissions(cs).stream().map(commissionMapper::commissionToSimpleCommissionDto).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get commission by id")
    public DetailedCommissionDto findById(@PathVariable Long id) {
        log.info("A user is fetching a commission with id '{}'", id);
        return commissionMapper.commissionToDetailedCommissionDto(commissionService.findById(id));
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Post commission")
    public void postCommission(@RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to create a commission.");
        try {
            commissionService.saveCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }

    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update commission")
    @Transactional
    public void updateCommission(@RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to update a commission.");
        try {
            commissionService.updateCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    //TODO: permit only admin
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @Operation(summary = "Delete commission")
    public void deleteCommission(@RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to delete a commission.");
        try {
            commissionService.deleteCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
