package com.westgoten.movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private Movie[] movieList;
    private MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();
    private boolean isActivatedByConfigChanges;
    private MutableLiveData<Boolean> isGetMoviesFinished = new MutableLiveData<>();
    private boolean activatedObserver;

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

    public boolean isActivatedByConfigChanges() {
        return isActivatedByConfigChanges;
    }

    public void setActivatedByConfigChanges(boolean activatedByConfigChanges) {
        this.isActivatedByConfigChanges = activatedByConfigChanges;
    }

    public LiveData<Boolean> getIsGetMoviesFinished() {
        return this.isGetMoviesFinished;
    }

    public void setIsGetMoviesFinished(Boolean isGetMoviesFinished) {
        this.isGetMoviesFinished.setValue(isGetMoviesFinished);
    }

    public boolean isActivatedObserver() {
        return activatedObserver;
    }

    public void setActivatedObserver(boolean activatedObserver) {
        this.activatedObserver = activatedObserver;
    }
}
