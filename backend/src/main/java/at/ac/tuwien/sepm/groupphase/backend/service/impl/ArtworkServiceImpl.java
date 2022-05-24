package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class ArtworkServiceImpl implements ArtworkService {
    private final ArtworkRepository artworkRepo;
    private  final ImageFileManager ifm;
    @Autowired
    public ArtworkServiceImpl(ArtworkRepository artworkRepo, ImageFileManager ifm) {
        this.artworkRepo = artworkRepo;
        this.ifm = ifm;
    }

    @Override
    public List<Artwork> findArtworksByArtist(Long id) {
        return artworkRepo.findArtworkByArtistId(id);
    }

    @Override
    public void saveArtwork(Artwork a) throws IOException {

        a.setImageUrl( this.ifm.writeArtistImage(a));
        this.artworkRepo.save(a);
    }

    @Override
    public void deleteArtwork(Artwork a) {
        this.artworkRepo.deleteById(a.getId());
        this.ifm.deleteArtistImage(a);
    }
    //todo fileSystem Implementation
    @Override
    public List<Artwork> searchArtworks(Specification<Artwork> spec) {
        return artworkRepo.findAll(spec);
    }

    @Override
    public List<Artwork> searchArtworks(Specification<Artwork> spec,  Pageable pageable) {
        if(spec==null){
            return  this.artworkRepo.findAll(pageable).getContent();
        }
        return  this.artworkRepo.findAll(spec, pageable).getContent();
    }
}
