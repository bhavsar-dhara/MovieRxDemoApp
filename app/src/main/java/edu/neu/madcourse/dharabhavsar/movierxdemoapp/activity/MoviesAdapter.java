package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.R;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.MovieResult;

/**
 * Created by Dhara on 7/2/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    @BindView(R.id.movie_poster)
    ImageView moviePoster;
    @BindView(R.id.btn_fav)
    ImageView btnFav;
    @BindView(R.id.movie_card)
    CardView movieCard;

    private List<MovieResult> movieResult;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_poster) ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    MoviesAdapter(List<MovieResult> movieResult, Context context) {
        this.movieResult = movieResult;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imageLink = "https://image.tmdb.org/t/p/w500" + movieResult.get(position).getPosterPath();
        try {
            Log.e(TAG, "in TRY Picasso .. link = " + imageLink);
            Picasso.with(holder.imageView.getContext())
                    .load(imageLink)
                    .placeholder(R.drawable.noun_748988_cc)
                    .error(R.drawable.noun_748988_cc)
                    .into(holder.imageView);
        } catch (Exception e) {
            Log.e(TAG, "in CATCH Picasso");
            holder.imageView.setImageResource(R.drawable.noun_748988_cc);
        }

//        Glide.with(holder.imageView.getContext())
//                .load(imageLink)
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieResult.size();
    }
}
