package com.westgoten.movies;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.*;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.westgoten.movies.fragments.MovieDetailsFragment;
import com.westgoten.movies.fragments.MovieListFragment;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private GetMovies getMoviesInstance;
    private MainActivityViewModel viewModel;
    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;

    private static final String LANGUAGE = "pt-BR";
    private static final int PAGE = 1;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.fragment_container);
        ProgressBar progressBar = (ProgressBar) getLayoutInflater().inflate(R.layout.progress_bar, frameLayout, false);

        fragmentManager = getSupportFragmentManager();
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getSelectedMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if(!viewModel.isActivatedByConfigChanges()) {
                    MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, movieDetailsFragment)
                            .addToBackStack(MovieDetailsFragment.FRAGMENT_NAME)
                            .commit();
                } else {
                    viewModel.setActivatedByConfigChanges(false);
                }
            }
        });

        viewModel.getIsGetMoviesFinished().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    frameLayout.removeViewAt(0);
                    MovieListFragment movieListFragment = new MovieListFragment();
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_container, movieListFragment)
                            .commit();
                    viewModel.setIsGetMoviesFinished(false);
                    viewModel.setActivatedObserver(true);
                }
            }
        });

        if (savedInstanceState == null) {
            frameLayout.addView(progressBar);
            getMoviesInstance = (GetMovies) new GetMovies().execute();
        } else {
            if (!viewModel.getIsGetMoviesFinished().getValue() && !viewModel.isActivatedObserver())
                frameLayout.addView(progressBar);
        }
    }

    @Override
    protected void onStop() {
        if (isChangingConfigurations())
            viewModel.setActivatedByConfigChanges(true);
        else
            viewModel.setActivatedByConfigChanges(false);

        super.onStop();
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
            viewModel.setIsGetMoviesFinished(false);
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
                    imagesBaseUrl = imagesMetaData.getSecure_base_url() + posterSizes[posterSizes.length-3];

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
                            Log.d("PosterPath", movie.getPoster_path());
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
            viewModel.setIsGetMoviesFinished(true);
        }
    }


}
