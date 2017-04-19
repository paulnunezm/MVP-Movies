package com.nunez.popularmovies.showMovieDetails

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.support.v7.graphics.Palette
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import com.nunez.popularmovies.runOnLollipopOrGreater

/**
 * Created by paulnunez on 4/18/17.
 */
class DetailsViewsColorChanger(
        val image: Bitmap, val titleBackground: View, val toolbar: Toolbar,
        val fab: ImageButton, val favoriteMovie: Boolean, val window: Window) {


    fun changeColors() {

        // Get colors from the image
        Palette.from(image).generate {
            if (it != null) {
                try {
                    val swatch = it.darkVibrantSwatch
                    val color = swatch?.rgb // get the vibrant color

                    if (color != null) {

                        val alphaColor = Color.argb(170, Color.red(color), Color.green(color),
                                Color.blue(color))

                        val fabColor = if (favoriteMovie) 0xFFF else color

                        // Set awesome colors to text and backgrounds
                        titleBackground.setBackgroundColor(alphaColor)
                        toolbar.setBackgroundColor(alphaColor)
                        fab.setColorFilter(fabColor)

                        runOnLollipopOrGreater { changeStatusBarColor(color) }

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor(color: Int) {
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        window.statusBarColor = color
    }

}
