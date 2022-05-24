package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.io.IOException;
import java.util.List;

public interface ArtistService {

    List<Artist> getAllArtists();
    Artist findArtistById(Long id);
    Artist saveArtist(Artist artist);

    void updateArtist(Artist artist) throws IOException;
}
