package com.amonge.sandbox.musicartisapi.service;

import com.amonge.sandbox.musicartisapi.config.GlobalConfig;
import com.amonge.sandbox.musicartisapi.pojo.Album;
import com.amonge.sandbox.musicartisapi.pojo.incoming.CoverArtArchiveResponse;
import com.amonge.sandbox.musicartisapi.pojo.incoming.MusicBrainzAlbum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * This service provides a cover image for the albums specified.
 * It queries an external service to get the cover image.
 */
@Service
public class AlbumCoverImageService {
    private static final Logger LOG = LoggerFactory.getLogger(AlbumCoverImageService.class);
    private final RestTemplate restTemplate;
    private final GlobalConfig config;

    public AlbumCoverImageService(RestTemplateBuilder restTemplateBuilder, GlobalConfig config) {
        restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public CompletableFuture<List<Album>> getAlbumsCoverInformation(List<MusicBrainzAlbum> albums) {
        // Get all albums cover image info asynchronously
        List<CompletableFuture<Album>> allAlbumsCoverInfoFutures = albums.stream()
                .map(this::getAlbumCoverInformation)
                .collect(Collectors.toList());

        // Create a combined future
        CompletableFuture<Void> allFuturesFuture = CompletableFuture.allOf(
                allAlbumsCoverInfoFutures.toArray(new CompletableFuture[0])
        );

        // When all futures are completed, get the results and collect them in a list
        return allFuturesFuture.thenApply(v -> allAlbumsCoverInfoFutures.stream()
                .map(albumCoverFuture -> albumCoverFuture.join())
                .collect(Collectors.toList()));
    }

    private CompletableFuture<Album> getAlbumCoverInformation(MusicBrainzAlbum album) {
        String url = String.format(config.getAlbumCoverServiceUrl(), album.getId());

        return CompletableFuture.supplyAsync(() -> restTemplate.getForObject(url, CoverArtArchiveResponse.class))
                .thenApply(response -> {
                    Album albumRecord = new Album();
                    albumRecord.setId(album.getId());
                    albumRecord.setTitle(album.getTitle());
                    albumRecord.setImage(response.getImages().get(0).getImage());

                    return albumRecord;
                })
                .exceptionally(e -> {
                    LOG.warn("Unable to get album cover information from [url={}] for [album={}]", url, album.getId(), e);

                    Album albumRecord = new Album();
                    albumRecord.setId(album.getId());
                    albumRecord.setTitle(album.getTitle());
                    return albumRecord;
                });
    }
}
