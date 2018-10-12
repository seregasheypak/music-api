package com.amonge.sandbox.musicartisapi.api;

import com.amonge.sandbox.musicartisapi.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * Rest controller, it retrieves information related to a music artist.
 */
@RestController
@RequestMapping("/api/v1/artist")
public class ArtistController {
    private static final Logger LOG = LoggerFactory.getLogger(ArtistController.class);

    private ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Get music artist information given an artist identifier.
     * @param artistId artist identifier
     * @return artist information
     */
    @RequestMapping("/{artistId}")
    public CompletableFuture getArtistInformation(@PathVariable String artistId) {
        return artistService.getArtist(artistId);
    }
}
