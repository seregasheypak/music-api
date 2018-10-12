package com.amonge.sandbox.musicartisapi.service;

import com.amonge.sandbox.musicartisapi.config.GlobalConfig;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMResponse;
import com.amonge.sandbox.musicartisapi.pojo.incoming.MusicBrainzArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * This service provides a brief description for an artist
 * It queries an external service to get the description.
 */
@Service
public class ArtistDescriptionService {
    private static final Logger LOG = LoggerFactory.getLogger(ArtistDescriptionService.class);

    private final RestTemplate restTemplate;
    private final GlobalConfig config;

    public ArtistDescriptionService(RestTemplateBuilder restTemplateBuilder, GlobalConfig config) {
        restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public CompletableFuture<String> getArtistDescription(MusicBrainzArtist artist) {
        String url = String.format(config.getArtistDescriptionServiceUrl(), config.getArtistDescriptionServiceAPIKey(), artist.getId());

        return CompletableFuture.supplyAsync(() ->
                restTemplate.getForObject(url, LastFMResponse.class))
                .thenApply(response -> response.getArtist().getBio().getSummary())
                .exceptionally( ex -> {
                    LOG.warn("Unable to get description from [url = {}] for [artist = {}]", url, artist.getId(), ex);
                    return null;
                });
    }
}
