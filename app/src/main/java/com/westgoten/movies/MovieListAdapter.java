package com.westgoten.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder> {
    private Movie[] dataset;
    private MainActivityViewModel activityViewModel;

    public MovieListAdapter(MainActivityViewModel activityViewModel) {
        this.dataset = activityViewModel.getMovieList();
        this.activityViewModel = activityViewModel;
    }

    public static class MovieListViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public  MovieListViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    @NonNull
    @Override
    public MovieListViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);

        return new MovieListViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListViewHolder holder, final int position) {
        Movie movie = dataset[position];

        ImageView poster = (ImageView) holder.cardView.getChildAt(0);
        poster.setImageBitmap(movie.getPoster());

        TextView title = (TextView) holder.cardView.getChildAt(1);
        title.setText(movie.getTitle());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityViewModel.setSelectedMovie(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.length;
    }
}