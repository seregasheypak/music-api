package com.amonge.sandbox.musicartisapi.service;

import com.amonge.sandbox.musicartisapi.config.GlobalConfig;
import com.amonge.sandbox.musicartisapi.exception.ArtistNotFoundException;
import com.amonge.sandbox.musicartisapi.exception.InternalErrorException;
import com.amonge.sandbox.musicartisapi.pojo.*;
import com.amonge.sandbox.musicartisapi.pojo.incoming.MusicBrainzArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This service provides detail information about an artist.
 * It queries external services to get the artist data and its details.
 */
@Service
public class ArtistService {
    private static final Logger LOG = LoggerFactory.getLogger(ArtistService.class);

    private final RestTemplate restTemplate;
    private final GlobalConfig config;
    private final AlbumCoverImageService albumCoverImageService;
    private final ArtistDescriptionService artistDescriptionService;

    public ArtistService(RestTemplateBuilder restTemplateBuilder, GlobalConfig config, AlbumCoverImageService albumCoverImageService, ArtistDescriptionService artistDescriptionService) {
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
        this.albumCoverImageService = albumCoverImageService;
        this.artistDescriptionService = artistDescriptionService;
    }

    public CompletableFuture getArtist(String artistId) {
        CompletableFuture<MusicBrainzArtist> artistInformationFuture = getArtistInformation(artistId);

        return artistInformationFuture.thenApply(artistInformation -> {

            CompletableFuture<List<Album>> albumsCoverImagesFuture = getArtistAlbumsCovers(artistInformation.getId());
            CompletableFuture<String> artistDescriptionFuture = getArtistDescription(artistInformation.getId());

            return albumsCoverImagesFuture.thenCombine(artistDescriptionFuture, (albumsCoverImages, artistDescription) -> generateArtistRecord(artistInformation, albumsCoverImages, artistDescription));
        });
    }

    private CompletableFuture<List<Album>> getArtistAlbumsCovers(String artistId) {
        return getArtistInformation(artistId).thenCompose(artistInformation -> albumCoverImageService.getAlbumsCoverInformation(artistInformation.getReleaseGroups()));
    }

    private CompletableFuture<String> getArtistDescription(String artistId) {
        return getArtistInformation(artistId).thenCompose(artistDescriptionService::getArtistDescription);
    }


    private CompletableFuture<MusicBrainzArtist> getArtistInformation(String artistId) throws ArtistNotFoundException {
        String url = String.format(config.getArtistInformationServiceUrl(), artistId);

        return CompletableFuture.supplyAsync(() -> {
            try {
                return restTemplate.getForObject(url, MusicBrainzArtist.class);
            } catch (HttpClientErrorException exception) {
                HttpStatus statusCode = exception.getStatusCode();

                if (statusCode.is4xxClientError()) { // will code be printed to log? can it help to determine a problem during log analysis in ELK / Splunk / Whatever
                    LOG.warn("Unable to find artist from [url = {}] for [artist = {}]", url, artistId, exception);
                    throw new ArtistNotFoundException("Unable to get information for artist: " + artistId, exception);
                } else {
                    LOG.error("Error while calling external API [url = {}] for [artist = {}]", url, artistId, exception);
                    throw exception;
                }
            } catch (Exception e) {
                LOG.error("Error while getting artist information from [url = {}] for [artist = {}]", url, artistId, e);
                throw new InternalErrorException("Error while getting artist information from third party provider", e);
            }
        });
    }

    private Artist generateArtistRecord(MusicBrainzArtist basicArtistInformation, List<Album> albums, String artistDescription) {
        Artist artist = new Artist();

        artist.setMbid(basicArtistInformation.getId());
        artist.setAlbums(albums);
        artist.setDescription(artistDescription);

        return artist;
    }

}
