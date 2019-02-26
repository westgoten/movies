package com.westgoten.movies;

public class PopularMovies {
    private Movie[] results;

    public PopularMovies(Movie[] results) {
        this.results = results;
    }

    public Movie[] getResults() {
        return results;
    }
}
