package com.nunez.popularmovies.showMovieDetails

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.nunez.popularmovies.model.entities.Review
import com.nunez.popularmovies.model.entities.ReviewsWrapper
import com.nunez.popularmovies.model.entities.Video
import com.nunez.popularmovies.model.entities.VideosWrapper
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by paulnunez on 4/18/17.
 */
class MovieDetailsPresenterTest {

    val view: MovieDetailsContract.View = mock()
    val controller: MovieDetailsContract.MovieDetailsController = mock()

    lateinit var presenter: MovieDetailsPresenter

    @Before
    fun setup() {
        presenter = MovieDetailsPresenter()
        presenter.attachView(view)
        presenter.setController(controller)
    }

    @Test
    @Throws(Exception::class)
    fun showReviewsShouldPassWithNullObjects() {
        // Given / Arrange
        val wrapper: ReviewsWrapper? = null
        val review: ArrayList<Review>? = null

        //When // Act
        presenter.showReviews(wrapper)

        // Then // assure
        verify(view, never()).showReviews(review)
    }

    @Test
    @Throws(Exception::class)
    fun showReviewsShouldPassWithNullReviews() {
        // Given / Arrange
        val reviews: ArrayList<Review>? = null
        val wrapper = ReviewsWrapper()
        wrapper.reviews = reviews

        //When // Act
        presenter.showReviews(wrapper)

        // Then // assure
        verify(view, never()).showReviews(reviews)
    }

    @Test
    @Throws(Exception::class)
    fun favoriteShouldPassWithFalse() {
        // Arrange/ given
        whenever(controller.checkIfFavorite()).thenReturn(false)

        //When//act
        presenter.checkIfFavorite()

        // Then // assure
        verify(view, never()).setFavorite()
    }

    @Test
    @Throws(Exception::class)
    fun favoriteShouldPassWithTrue() {
        // Arrange/ given
        whenever(controller.checkIfFavorite()).thenReturn(true)

        //When//act
        presenter.checkIfFavorite()

        // Then // assure
        verify(view).setFavorite()
    }

    @Test
    @Throws(Exception::class)
    fun ratingShouldHandleNullEntries() {
        // Arrange/ given

        //When//act
        presenter.showRatings(null)

        // Then // assure
        verify(view, never()).showRatings(null)
    }

    @Test
    @Throws(Exception::class)
    fun ratingShouldHandleEmptyStrings() {
        // Arrange/ given

        //When//act
        presenter.showRatings("")

        // Then // assure
        verify(view, never()).showRatings("")
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowNullTrailers() {
        // Arrange/ given

        //When//act
        presenter.showTrailers(null)

        // Then // assure
        verify(view, never()).showTrailers(null)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowNullTrailersVideos() {
        // Arrange/ given
        var trailersWrapper = VideosWrapper()
        val trailers: ArrayList<Video>? = null
        trailersWrapper.setVideos(trailers)

        //When//act
        presenter.showTrailers(trailersWrapper)

        // Then // assure
        verify(view, never()).showTrailers(trailers)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowEmptyTrailersVideos() {
        // Arrange/ given
        var trailersWrapper = VideosWrapper()
        val trailers = ArrayList<Video>()
        trailersWrapper.setVideos(trailers)

        //When//act
        presenter.showTrailers(trailersWrapper)

        // Then // assure
        verify(view, never()).showTrailers(trailers)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowTrailerLink() {
        // Arrange/ given

        //When//act
        presenter.setTrailerLink(null)

        // Then // assure
        verify(view, never()).showTrailers(null)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowTrailerLinkWithEmptyVideos() {
        // Arrange/ given
        val url = VideosWrapper()
        url.videos = ArrayList<Video>()

        //When//act
        presenter.setTrailerLink(url)

        // Then // assure
        verify(view, never()).showTrailers(url.videos)
    }
}