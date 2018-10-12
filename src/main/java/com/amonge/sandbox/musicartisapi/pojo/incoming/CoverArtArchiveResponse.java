package com.amonge.sandbox.musicartisapi.pojo.incoming;

import java.util.List;

public class CoverArtArchiveResponse {
    private String release;
    private List<CoverArtArchiveImage> images;


    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public List<CoverArtArchiveImage> getImages() {
        return images;
    }

    public void setImages(List<CoverArtArchiveImage> images) {
        this.images = images;
    }
}
