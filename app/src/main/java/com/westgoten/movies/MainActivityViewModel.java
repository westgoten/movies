package com.westgoten.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private Movie[] movieList;
    private MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();

    public Movie[] getMovieList() {
        return movieList;
    }

    public void setMovieList(Movie[] movieList) {
        this.movieList = movieList;
    }

    public LiveData<Movie> getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(int position) {
        this.selectedMovie.setValue(movieList[position]);
    }
}
