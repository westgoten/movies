package com.westgoten.movies;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Movie {
    private String poster_path;
    private String overview;
    private String title;
    private double popularity;
    private Bitmap poster;

    public Movie(String poster_path, String overview, String title, double popularity) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.title = title;
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public double getPopularity() {
        return popularity;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }
}
