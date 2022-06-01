package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TagMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class TagMappingTest {

    @Autowired
    private TagService tagService;

    @Autowired
    private TagMapper tagMapper;

    private Tag tag;

    public Tag getTag() {
        return tag = new Tag("aTag");
    }

    @Test
    public void givenNothing_whenMapDetailedTagDtoToEntity_thenEntityHasAllProperties() throws Exception {
        Tag tag = getTag();
        TagDto tagDto = tagMapper.tagToTagDto(tag);

        assertEquals("aTag", tagDto.getName());
        assertEquals(null, tagDto.getId());
    }
}
