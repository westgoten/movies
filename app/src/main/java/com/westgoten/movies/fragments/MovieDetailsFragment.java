package com.westgoten.movies.fragments;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProviders;
import com.westgoten.movies.MainActivityViewModel;
import com.westgoten.movies.Movie;
import com.westgoten.movies.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MovieDetailsFragment extends Fragment {
    public static final String FRAGMENT_NAME = "MovieDetails";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivityViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_movie_details, container, false);

        ImageView poster = (ImageView) linearLayout.getChildAt(0);
        TextView title = (TextView) linearLayout.getChildAt(1);
        ScrollView scrollView = (ScrollView) linearLayout.getChildAt(2);
        TextView overview = (TextView) scrollView.getChildAt(0);

        Movie selectedMovie = viewModel.getSelectedMovie().getValue();
        poster.setImageBitmap(selectedMovie.getPoster());
        title.setText(selectedMovie.getTitle());
        String overviewText = selectedMovie.getOverview();
        if (!overviewText.trim().isEmpty())
            overview.setText(overviewText);
        else
            overview.setText(getString(R.string.no_description));

        return linearLayout;
    }
}
