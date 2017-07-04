package edu.neu.madcourse.dharabhavsar.movierxdemoapp.service;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.Movies;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.Constant;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dhara on 3/15/2017.
 */

public interface TMDbService {

    @GET(Constant.DISCOVER_MOVIE)
    Observable<Movies> getMovies(@Query("api_key") String apiKey);

    @GET(Constant.DISCOVER_MOVIE)
    Observable<Movies> getMovies(@Query("api_key") String apiKey, @Query("page") int pageNo);
}
