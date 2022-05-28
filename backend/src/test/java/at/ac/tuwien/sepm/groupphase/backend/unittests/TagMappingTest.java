package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TagMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void givenNothing_whenMapDetailedArtworkDtoToEntity_thenEntityHasAllProperties() throws Exception {
        Tag tag = getTag();
        TagDto tagDto = tagMapper.tagToTagDto(tag);

        assertEquals("aTag", tagDto.getName());
        assertEquals(0, tagDto.getId());
    }
}
