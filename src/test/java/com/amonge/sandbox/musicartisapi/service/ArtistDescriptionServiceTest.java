package com.amonge.sandbox.musicartisapi.service;

import com.amonge.sandbox.musicartisapi.config.GlobalConfig;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMArtist;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMBio;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMResponse;
import com.amonge.sandbox.musicartisapi.pojo.incoming.MusicBrainzArtist;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArtistDescriptionServiceTest {
    private RestTemplateBuilder restTemplateBuilderMock;
    private RestTemplate restTemplateMock;
    private GlobalConfig globalConfigMock;

    @Before
    public void setup() {
        restTemplateBuilderMock = mock(RestTemplateBuilder.class);
        restTemplateMock = mock(RestTemplate.class);
        globalConfigMock = mock(GlobalConfig.class);

        // RestTemplate mock setup
        when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);

        // GlobalConfig mock setup
        when(globalConfigMock.getArtistDescriptionServiceUrl()).thenReturn("some url");
        when(globalConfigMock.getArtistDescriptionServiceAPIKey()).thenReturn("some api key");
    }

    @Test
    public void getArtistDescription() throws ExecutionException, InterruptedException {
        MusicBrainzArtist inputTestArtist = getArtistMock("some id");
        LastFMResponse serviceResponseMock = getServiceResponseMock();

        when(restTemplateMock.getForObject(anyString(), any())).thenReturn(getServiceResponseMock());
        ArtistDescriptionService service = new ArtistDescriptionService(restTemplateBuilderMock, globalConfigMock);


        CompletableFuture<String> artistDescriptionFuture = service.getArtistDescription(inputTestArtist);

        assertEquals(serviceResponseMock.getArtist().getBio().getSummary(), artistDescriptionFuture.get());
    }

    @Test
    public void getArtistDescription_ExternalServiceException() throws ExecutionException, InterruptedException {
        MusicBrainzArtist inputTestArtist = getArtistMock("some id");

        when(restTemplateMock.getForObject(anyString(), any())).thenThrow(HttpClientErrorException.class);
        ArtistDescriptionService service = new ArtistDescriptionService(restTemplateBuilderMock, globalConfigMock);


        CompletableFuture<String> artistDescriptionFuture = service.getArtistDescription(inputTestArtist);

        assertNull(artistDescriptionFuture.get());
    }

    @Test
    public void getArtistDescription_InternalException() throws ExecutionException, InterruptedException {
        MusicBrainzArtist inputTestArtist = getArtistMock("some id");
        when(restTemplateMock.getForObject(anyString(), any())).thenThrow(RuntimeException.class);

        ArtistDescriptionService service = new ArtistDescriptionService(restTemplateBuilderMock, globalConfigMock);


        CompletableFuture<String> artistDescriptionFuture = service.getArtistDescription(inputTestArtist);

        assertNull(artistDescriptionFuture.get());
    }

    private MusicBrainzArtist getArtistMock(String artistId) {
        MusicBrainzArtist artist = new MusicBrainzArtist();
        artist.setId(artistId);

        return artist;
    }

    private LastFMResponse getServiceResponseMock() {
        LastFMBio lastFMBio = new LastFMBio();
        lastFMBio.setSummary("some description");
        LastFMArtist lastFMArtist = new LastFMArtist();
        lastFMArtist.setBio(lastFMBio);
        LastFMResponse serviceResponse = new LastFMResponse();
        serviceResponse.setArtist(lastFMArtist);

        return serviceResponse;
    }
}