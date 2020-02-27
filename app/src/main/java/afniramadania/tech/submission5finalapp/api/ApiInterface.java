package afniramadania.tech.submission5finalapp.api;

import afniramadania.tech.submission5finalapp.models.Movie;
import afniramadania.tech.submission5finalapp.models.MovieGenresResponse;
import afniramadania.tech.submission5finalapp.models.MoviesResponse;
import afniramadania.tech.submission5finalapp.models.TvGenresResponse;
import afniramadania.tech.submission5finalapp.models.TvResponse;
import afniramadania.tech.submission5finalapp.models.Tvshow;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("discover/movie")
    Call<MoviesResponse> getMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("discover/movie")
    Call<MoviesResponse> getReleaseToday(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String gteDate,
            @Query("primary_release_date.lte") String lteDate
    );

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("genre/movie/list")
    Call<MovieGenresResponse> getMovieGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("discover/tv")
    Call<TvResponse> getTvs(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("search/tv")
    Call<TvResponse> searchTv(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );

    @GET("genre/tv/list")
    Call<TvGenresResponse> getTvGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_id}")
    Call<Tvshow> getTv(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );
}
