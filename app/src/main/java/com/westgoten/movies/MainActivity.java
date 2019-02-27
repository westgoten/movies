package com.westgoten.movies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.westgoten.movies.fragments.MovieListFragment;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private GetMovies getMoviesInstance;
    private MainActivityViewModel viewModel;

    private static final String LANGUAGE = "pt-BR";
    private static final int PAGE = 1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        if (savedInstanceState == null) {
            getMoviesInstance = (GetMovies) new GetMovies().execute();
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        if (isFinishing() && getMoviesInstance != null)
          getMoviesInstance.cancel(true);

        super.onDestroy();
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
                Log.e("FetchingConfiguration", e.getMessage());
            }

            Call<PopularMovies> popularMoviesCall = theMovieDbService.getPopularMovies(getString(R.string.api_key),
                    LANGUAGE, PAGE);
            try {
                Response<PopularMovies> popularMoviesResponse = popularMoviesCall.execute();
                if (popularMoviesResponse.isSuccessful()) {
                    PopularMovies popularMovies = popularMoviesResponse.body();
                    viewModel.setMovieList(popularMovies.getResults());
                    if (imagesBaseUrl != null) {
                        for (Movie movie : viewModel.getMovieList()) {
                            movie.setPoster_path(imagesBaseUrl + movie.getPoster_path());
                        }
                    }
                } else {
                    Log.e("PopularMoviesResponse", popularMoviesResponse.message());
                }
            } catch (IOException e) {
                Log.e("FetchingPopularMovies", e.getMessage());
            }

            try {
                for (Movie movie : viewModel.getMovieList()) {
                    Bitmap poster = Picasso.get().load(movie.getPoster_path())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .get();

                    movie.setPoster(poster);
                    if (isCancelled())
                        return null;
                }
            } catch (IOException e) {
                Log.e("FetchingBitmap", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // TO DO: Instantiate RecyclerView adapter

            MovieListFragment movieListFragment = new MovieListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, movieListFragment)
                    .commit();
        }
    }
}
