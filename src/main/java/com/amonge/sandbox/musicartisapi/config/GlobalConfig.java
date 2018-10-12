package com.amonge.sandbox.musicartisapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "globalConfiguration")
public class GlobalConfig {
    private String artistInformationServiceUrl;
    private String albumCoverServiceUrl ;
    private String artistDescriptionServiceUrl;
    private String artistDescriptionServiceAPIKey;

    public GlobalConfig() {
    }


    public String getArtistInformationServiceUrl() {
        return artistInformationServiceUrl;
    }

    @Value("${artistInformationService.url}")
    public void setArtistInformationServiceUrl(String artistInformationServiceUrl) {
        this.artistInformationServiceUrl = artistInformationServiceUrl;
    }

    public String getAlbumCoverServiceUrl() {
        return albumCoverServiceUrl;
    }

    @Value("${albumCoverService.url}")
    public void setAlbumCoverServiceUrl(String albumCoverServiceUrl) {
        this.albumCoverServiceUrl = albumCoverServiceUrl;
    }

    public String getArtistDescriptionServiceUrl() {
        return artistDescriptionServiceUrl;
    }

    @Value("${artistDescriptionService.url}")
    public void setArtistDescriptionServiceUrl(String artistDescriptionServiceUrl) {
        this.artistDescriptionServiceUrl = artistDescriptionServiceUrl;
    }

    public String getArtistDescriptionServiceAPIKey() {
        return artistDescriptionServiceAPIKey;
    }

    @Value("${artistDescriptionService.apiKey}")
    public void setArtistDescriptionServiceAPIKey(String artistDescriptionServiceAPIKey) {
        this.artistDescriptionServiceAPIKey = artistDescriptionServiceAPIKey;
    }
}