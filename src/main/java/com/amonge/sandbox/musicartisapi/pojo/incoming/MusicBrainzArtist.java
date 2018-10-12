package com.amonge.sandbox.musicartisapi.pojo.incoming;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MusicBrainzArtist {
    private String id;
    private String name;

    @JsonProperty("release-groups")
    private List<MusicBrainzAlbum> releaseGroups;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MusicBrainzAlbum> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<MusicBrainzAlbum> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }
}
