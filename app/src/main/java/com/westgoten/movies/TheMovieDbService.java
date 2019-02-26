package com.westgoten.movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheMovieDbService {
    String BASE_URL = "https://api.themoviedb.org/3/";

    @GET("movie/popular")
    Call<PopularMovies> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String lang,
                                         @Query("page") int page);
    @GET("configuration")
    Call<Configuration> getConfiguration(@Query("api_key") String apiKey);
}
