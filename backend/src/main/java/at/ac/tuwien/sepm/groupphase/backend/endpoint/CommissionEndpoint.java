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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RestController
@Validated
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
                                                       @RequestParam(defaultValue = "None", name = "date") SearchConstraint dateConstraint
    ) {
        log.info("A user is trying to search for a commission.");

        CommissionSearchDto cs = generateSearchDto(name, artistId, dateConstraint, priceRangeLower, priceRangeUpper, pageNr, userId);

        return commissionService.searchCommissions(cs).stream().map(commissionMapper::commissionToSimpleCommissionDto).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search/detailed")
    @Operation(summary = "Get all detailed commissions filtered")
    @Transactional
    public List<DetailedCommissionDto> searchDetailed(@RequestParam(defaultValue = "", name = "name") String name,
                                                      @RequestParam(defaultValue = "0", name = "pageNr") String pageNr,
                                                      @RequestParam(defaultValue = "50000", name = "priceRangeUpper") String priceRangeUpper,
                                                      @RequestParam(defaultValue = "0", name = "priceRangeLower") String priceRangeLower,
                                                      @RequestParam(defaultValue = "", name = "artistId") String artistId,
                                                      @RequestParam(defaultValue = "", name = "userId") String userId,
                                                      @RequestParam(defaultValue = "None", name = "dateOrder") SearchConstraint dateConstraint
    ) {
        log.info("A user is trying to search for detailed commissions.");

        CommissionSearchDto cs = generateSearchDto(name, artistId, dateConstraint, priceRangeLower, priceRangeUpper, pageNr, userId);

        return commissionService.searchCommissions(cs).stream().map(commissionMapper::commissionToDetailedCommissionDto).collect(Collectors.toList());
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
    public DetailedCommissionDto postCommission(@Valid @RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to create a commission.");

        return commissionMapper
            .commissionToDetailedCommissionDto(
                commissionService.saveCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto)));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update commission")
    @Transactional
    public void updateCommission(@Valid @RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to update a commission: " + commissionDto.toString());

        commissionService.updateCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping
    @Operation(summary = "Delete commission")
    public void deleteCommission(@RequestBody DetailedCommissionDto commissionDto) {
        log.info("A user is trying to delete a commission.");

        commissionService.deleteCommission(commissionMapper.detailedCommissionDtoToCommission(commissionDto));
    }

    private CommissionSearchDto generateSearchDto(String name, String artistId, SearchConstraint dateConstraint, String priceRangeLower, String priceRangeUpper, String pageNr, String userId) {
        CommissionSearchDto cs = new CommissionSearchDto();
        cs.setName(name.toLowerCase());
        cs.setArtistId(artistId);
        cs.setDateOrder(dateConstraint);
        cs.setPriceRangeLower(priceRangeLower);
        cs.setPriceRangeUpper(priceRangeUpper);
        cs.setPageNr(pageNr);
        cs.setUserId(userId);
        return cs;
    }
}
