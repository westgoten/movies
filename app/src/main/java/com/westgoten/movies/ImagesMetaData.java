package com.westgoten.movies;

public class ImagesMetaData {
    private String base_url;
    private String secure_base_url;
    private String[] poster_sizes;

    public ImagesMetaData(String base_url, String secure_base_url, String[] poster_sizes) {
        this.base_url = base_url;
        this.secure_base_url = secure_base_url;
        this.poster_sizes = poster_sizes;
    }

    public String getBase_url() {
        return base_url;
    }

    public String getSecure_base_url() {
        return secure_base_url;
    }

    public String[] getPoster_sizes() {
        return poster_sizes;
    }
}
