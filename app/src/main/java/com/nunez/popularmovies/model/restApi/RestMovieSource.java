package com.nunez.popularmovies.model.restApi;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nunez.popularmovies.BuildConfig;
import com.nunez.popularmovies.model.entities.Movie;
import com.nunez.popularmovies.model.entities.MovieDetails;
import com.nunez.popularmovies.model.entities.MoviesWrapper;
import com.nunez.popularmovies.model.entities.ReviewsWrapper;
import com.nunez.popularmovies.model.entities.VideosWrapper;
import com.nunez.popularmovies.utils.Callbacks;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * This class is an implementation of the {@link RestDataSource}
 * It manages queryng
 */
public class RestMovieSource implements RestDataSource{
    private static String LOG_TAG = RestMovieSource.class.getSimpleName();

    private  OkHttpClient                  client = new OkHttpClient();
    private  Response                      response;
    private  Callbacks.StandarCallback     controllerCallback;
    private  MovieDatabaseApi              mDbApi;
    private  Context                       mContext;

    public RestMovieSource(Callbacks.StandarCallback controllerCallback){ // Needs a callback for the controller
        this.controllerCallback = controllerCallback;
//        mDbApi = new MovieDatabaseApi(PopularMovies.getApiKey());
        mDbApi = new MovieDatabaseApi(BuildConfig.theMovieDbApiKey);

        /**
         * This let us see all the network flow in the chrome developer tools
         */
        client.networkInterceptors().add(new StethoInterceptor());

    }

    @Override
    public void getMoviesByPage(int page) {

    }

    @Override
    public void getMovies() {
        new GetMoviesCall().execute();
    }

    @Override
    public void getMovieDetails(String id) {
        Log.d(LOG_TAG, "onGetMovieDetails");
        MovieDetails movie = new MovieDetails();
        movie.setId(id);

        GetMovieDetailsCall mCall = new GetMovieDetailsCall(movie);
        mCall.execute();
    }


    public void onSuccess(Object response) {

        Log.d(LOG_TAG, "onSuccess RestMovie Source");
        Log.d(LOG_TAG, response.getClass().getSimpleName());


//        controllerCallback.onSuccess(movies);


        if(response instanceof Movie){

            Log.d(LOG_TAG, "instance of movie");

            Log.d(LOG_TAG, ((Movie) response).getId());
            controllerCallback.onSuccess(response);

        }else if( response instanceof MoviesWrapper){

            controllerCallback.onSuccess( response);

        }else if( response instanceof  MovieDetails){
            controllerCallback.onSuccess(response);
        }
    }


    public void onError(String e){
        controllerCallback.onError(e);
    }

    class GetMoviesCall extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url(mDbApi.getPopularMovies())
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return  response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                onError(e.toString());
                Log.d("RestMovieSource", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            MoviesWrapper response = gson.fromJson(result, MoviesWrapper.class);

            if(response != null){
                onSuccess(response);
            }

        }
    }

    class GetMovieDetailsCall extends AsyncTask<Void, Void, String> {

        private MovieDetails mMovie;

        public GetMovieDetailsCall(MovieDetails movie){
            mMovie = movie;
        }


        @Override
        protected String doInBackground(Void... params) {
            Log.d("GetMovieDetailsCall", "onDoInBack");


            Request request = new Request.Builder()
                    .url(mDbApi.getMovieDetails(mMovie.getId()))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return  response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                onError(e.toString());
                Log.d("RestMovieSource", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GetMovieDetailsCall", "onPostEx");

            Gson gson = new Gson();

//            Movie response = null;
            mMovie  = null;
            try {
                mMovie = gson.fromJson(result, MovieDetails.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if(mMovie != null){
                Log.d("GetMovieDetailsCall", mMovie.title);
//                onSuccess(response);
//                mMovie = response
                new GetVideoMovieVideosCall(mMovie).execute();
            }

        }
    }

    class GetVideoMovieVideosCall extends AsyncTask<Void, Void, String> {

        public MovieDetails mMovie;

        public GetVideoMovieVideosCall(MovieDetails movie) {
            mMovie = movie;
        }

        @Override
        protected String doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url(mDbApi.getMovieVideo(mMovie.getId()))
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return  response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                onError(e.toString());
                Log.d("RestMovieSource", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Gson gson = new Gson();
            VideosWrapper response = null;
            try {
                response = gson.fromJson(result, VideosWrapper.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if(response != null){
//                return response;
                mMovie.setVideosWrapper(response);
//                onSuccess(mMovie);
                new GetMovieReviewsCall(mMovie).execute();
            }

        }
    }

    class GetMovieReviewsCall extends AsyncTask<Void, Void, String> {

        public MovieDetails mMovie;

        public GetMovieReviewsCall(MovieDetails movie) {
            mMovie = movie;
        }

        @Override
        protected String doInBackground(Void... params) {
            Request request = new Request.Builder()
                    .url(mDbApi.getMovieReviews(mMovie.getId()))
                    .build();

            Log.d("GetMovieReviewsCall", "doInBackground");

            try {
                Response response = client.newCall(request).execute();
                return  response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                onError(e.toString());
//                Log.d("GetMovieReviewsCall", e.toString());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d("GetMovieReviewsCall", "onPostExecute");

            Gson gson = new Gson();
            ReviewsWrapper response = null;
            try {
                if(!result.isEmpty()) response = gson.fromJson(result, ReviewsWrapper.class);
                mMovie.setReviewsWrapper(response);
                onSuccess(mMovie);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            onSuccess(mMovie);
        }
    }
}
