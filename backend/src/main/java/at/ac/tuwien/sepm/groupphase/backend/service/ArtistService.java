package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import java.util.List;

public interface ArtistService {

    List<Artist> getAllArtists();
    Artist findArtistById(Long id);
}
