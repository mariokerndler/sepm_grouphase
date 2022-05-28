package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TagMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/tag")
@Slf4j
public class TagEndpoint {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TagService tagService;
    private final TagMapper tagMapper;

    @Autowired
    public TagEndpoint(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Load all Tags")
    public List<TagDto> loadAllTags() {
        log.debug("Get /Tags");
        try {
            return tagService.loadAllTags().stream().map(tagMapper::tagToTagDto).collect(Collectors.toList());
        } catch (NotFoundException n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

}
