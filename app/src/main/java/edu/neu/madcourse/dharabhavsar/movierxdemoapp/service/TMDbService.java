package edu.neu.madcourse.dharabhavsar.movierxdemoapp.service;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.Movie;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Dhara on 3/15/2017.
 */

public interface TMDbService {

    String SERVICE_ENDPOINT = "https://api.themoviedb.org/3";
    //note: you don't need to define this URL String here

    @GET("/discover/movie")
    Observable<Movie> getMovie();
}
