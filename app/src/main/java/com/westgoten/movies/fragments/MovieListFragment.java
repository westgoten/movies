package com.westgoten.movies.fragments;

import com.westgoten.movies.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.westgoten.movies.MainActivityViewModel;
import com.westgoten.movies.MovieListAdapter;

public class MovieListFragment extends Fragment {
    private MainActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        MovieListAdapter movieListAdapter = new MovieListAdapter(viewModel);
        recyclerView.setAdapter(movieListAdapter);

        return recyclerView;
    }
}
