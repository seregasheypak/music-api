package com.amonge.sandbox.musicartisapi.service;

import com.amonge.sandbox.musicartisapi.config.GlobalConfig;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMArtist;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMBio;
import com.amonge.sandbox.musicartisapi.pojo.incoming.LastFMResponse;
import com.amonge.sandbox.musicartisapi.pojo.incoming.MusicBrainzArtist;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
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
        when(restTemplateMock.getForObject(anyString(), any())).thenReturn(getServiceResponseMock());

        // GlobalConfig mock setup
        when(globalConfigMock.getArtistDescriptionServiceUrl()).thenReturn("some url");
        when(globalConfigMock.getArtistDescriptionServiceAPIKey()).thenReturn("some api key");
    }

    @Test
    public void getArtistDescription() throws ExecutionException, InterruptedException {
        MusicBrainzArtist inputTestArtist = getArtistMock("some id");
        ArtistDescriptionService service = new ArtistDescriptionService(restTemplateBuilderMock, globalConfigMock);

        CompletableFuture<String> artistDescriptionFuture = service.getArtistDescription(inputTestArtist);

        assertEquals("some description", artistDescriptionFuture.get());
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