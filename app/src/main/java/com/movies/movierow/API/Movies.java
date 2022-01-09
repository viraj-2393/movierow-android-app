package com.movies.movierow.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.movies.movierow.Models.CastDetails;
import com.movies.movierow.Models.KidMoviesModel;
import com.movies.movierow.Models.MovieID;
import com.movies.movierow.Models.PopularMoviesModel;
import com.movies.movierow.Models.Trailer;
import com.movies.movierow.Models.TvShowModel;
import com.movies.movierow.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Movies {
    @GET("discover/movie/")
    Call<PopularMoviesModel> getMovies(@Query("api_key") String api_key,@Query("language") String language,@Query("sort_by") String sort_by,
                                       @Query("include_adult") boolean include_adult,@Query("include_video") boolean include_video,
                                       @Query("page") int page,@Query("with_watch_monetization_types") String watchMType);

    @GET("discover/movie/")
    Call<KidMoviesModel> getKidMovies(@Query("api_key") String api_key, @Query("certification_country") String certification_country,
                                      @Query("certification.lte") String certification_lte, @Query("with_genres") int with_genres,
                                      @Query("include_adult") boolean include_adult, @Query("sort_by") String sort_by);

    @GET("tv/popular")
    Call<TvShowModel> getTvShows(@Query("api_key") String api_key,@Query("language") String language, @Query("page") int page);

    @GET("search/movie/")
    Call<PopularMoviesModel> getSpecificMovie(@Query("api_key") String api_key,@Query("query") String query);

    @GET("search/tv/")
    Call<TvShowModel> getSpecificTv(@Query("api_key") String api_key,@Query("query") String query);

    @GET("search")
    Call<Trailer> getTrailer(@Query("part") String part,@Query("maxResults") int maxResults,@Query("q") String q,@Query("key") String api_key);

    @GET("similar")
    Call<PopularMoviesModel> getSimilarMovies(@Query("api_key") String api_key,@Query("language") String language, @Query("page") int page);

    @GET("credits")
    Call<CastDetails> getAllCast(@Query("api_key") String api_key,@Query("language") String language);

    @POST("auth/login")
    Call<UserModel> login(@Body JsonObject jsonObject);

    @POST("auth/signup")
    Call<UserModel> register(@Body JsonObject jsonObject);


    @PATCH("favMovie")
    Call<Object> setFavMovie(@Header("Authorization") String token, @Body JsonObject jsonObject);

    @GET("favMovie")
    Call<MovieID> getFavMovie(@Header("Authorization") String token);

    @GET("/")
    Call<UserModel> validateToken(@Header("Authorization") String token);

    @POST("auth/sendotp")
    Call<Object> sendOtp(@Body JsonObject jsonObject);

    @POST("auth/resetpasswordotp")
    Call<Object> sendResetPasswordOtp(@Body JsonObject jsonObject);

    @POST("auth/resetpassword")
    Call<Object> resetUserPass(@Body JsonObject jsonObject);



}
