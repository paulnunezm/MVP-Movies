##MVP-Movies
This a movies application that demonstrates the use of multiple libraries and
implementing a clean architecture fetching the data from [The Movie Database](https://www.themoviedb.org) Api.

###Libraries
- **[OkHttp](https://square.github.io/okhttp/)** As a http client.
- **[Stetho](https://facebook.github.io/stetho/)** For intercepting the data requested and view it with chrome.
- **[Support Palette](https://developer.android.com/reference/android/support/v7/graphics/Palette.html)** For retrieving the predominant colors in a picture. 
- **[Glide](https://github.com/bumptech/glide)** To perform image request and loading it to the views.
- **[Gson](https://github.com/google/gson)** For parsing the network's JSON response.

###Architecture
This app uses the [Model View Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) architecture as explain in [this article](https://saulmm.github.io/2015/02/02/A%20useful%20stack%20on%20android%20%231,%20architecture/)
written by [Saul Molinero](https://plus.google.com/+SaulMolineroMalvido) at his awesome blog. With a few modifications.

Instead of dividing the entire project in the three main layers of the architecture I opted to divide it
in features:

![Feature](/screenshots/feature.png?raw=true)

Instead of using [Otto](https://github.com/square/otto) for the layers communications right now is still using
callbacks interfaces and instead of using multiples classes for extending the base view, presenter and usecases
is done by creating a contract interface for the feature in which each one is extending the base interface.

```
public interface MovieDetailsContract {

    interface Presenter extends com.nunez.popularmovies.mvp.presenters.Presenter { 
        void attachView(View detailsView);
        void setTrailerLink();
        void showViews();
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers();
        void showReviews(); 
    }

    interface View extends MVPView{
        void setTrailerLink(String url);
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers(ArrayList<Video> trailers);
        void showReviews(ArrayList<Review> reviews); 
    } 
    
     interface MovieDetailsController extends UseCase{
         void requestMovieDetails();
         void sendMovieDetailsToPresenter(Movie movie);
    }
}

```

###Screenshots
![1](/screenshots/Screenshot_1.png?raw=true)
![2](/screenshots/Screenshot_2.png?raw=true)

