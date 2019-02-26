package com.westgoten.movies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Movie[] movieList;

    private static final String LANGUAGE = "pt-BR";
    private static final int PAGE = 1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new GetMovies().execute();
    }

    private class GetMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), getString(R.string.background_task), Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TheMovieDbService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TheMovieDbService theMovieDbService = retrofit.create(TheMovieDbService.class);

            Call<Configuration> configurationCall = theMovieDbService.getConfiguration(getString(R.string.api_key));
            String imagesBaseUrl = null;
            try {
                Response<Configuration> configurationResponse = configurationCall.execute();
                if (configurationResponse.isSuccessful()) {
                    Configuration configuration = configurationResponse.body();
                    ImagesMetaData imagesMetaData = configuration.getImages();
                    String[] posterSizes = imagesMetaData.getPoster_sizes();
                    imagesBaseUrl = imagesMetaData.getSecure_base_url() + posterSizes[posterSizes.length-2];

                } else {
                    Log.e("ConfigurationResponse", configurationResponse.message());
                }
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }

            Call<PopularMovies> popularMoviesCall = theMovieDbService.getPopularMovies(getString(R.string.api_key),
                    LANGUAGE, PAGE);
            try {
                Response<PopularMovies> popularMoviesResponse = popularMoviesCall.execute();
                if (popularMoviesResponse.isSuccessful()) {
                    PopularMovies popularMovies = popularMoviesResponse.body();
                    movieList = popularMovies.getResults();
                    if (imagesBaseUrl != null) {
                        for (Movie movie : movieList) {
                            movie.setPoster_path(imagesBaseUrl + movie.getPoster_path());
                        }
                    }
                } else {
                    Log.e("PopularMoviesResponse", popularMoviesResponse.message());
                }
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // TO DO: Instantiate RecyclerView adapter
            for (Movie movie : movieList) {
                Log.d("Title", movie.getTitle());
                Log.d("Overview", movie.getOverview());
                Log.d("PosterPath", movie.getPoster_path());
            }
        }
    }
}
