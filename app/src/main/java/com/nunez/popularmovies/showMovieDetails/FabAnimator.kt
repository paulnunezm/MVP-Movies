package com.nunez.popularmovies.showMovieDetails

import android.animation.*
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import com.nunez.popularmovies.R

/**
 * Created by paulnunez on 4/18/17.
 */
class FabAnimator(val fab: ImageButton, val heartInitialColor: Int, val resources: Resources) {


    val scaleValue = 1.3f
    var canClickFab = true
    var scaleAnimInitialized = false
    lateinit var scaleAnim: ObjectAnimator
    lateinit var rotateAnim: ObjectAnimator


    private fun prepareScaleAnimation() {
        scaleAnimInitialized = true
        val pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, scaleValue)
        val pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleValue)
        scaleAnim = ObjectAnimator.ofPropertyValuesHolder(fab, pvhX, pvhY)
        scaleAnim.duration = 500
        scaleAnim.repeatCount = 1
        scaleAnim.repeatMode = ValueAnimator.REVERSE
    }

    private fun prepareRotateAnimation(favorite: Boolean) {
        val rotation = if (favorite) 720f else 0f
        rotateAnim = ObjectAnimator.ofFloat(fab, "rotation", rotation)
        rotateAnim.interpolator = DecelerateInterpolator()
        rotateAnim.duration = 1400
    }


    fun animateFab(favorite: Boolean) {
        if (canClickFab) {

            prepareRotateAnimation(favorite)
            if (!scaleAnimInitialized) prepareScaleAnimation()

            //Let's change background's color to red or white
            val backgroundColorReference = if (favorite) R.drawable.fab else R.drawable.circle_white
            val color = arrayOf<Drawable>(fab.background, resources.getDrawable(backgroundColorReference))
            val trans = TransitionDrawable(color)

            //This will work also on old devices. The latest API says you have to use setBackground instead.
            fab.setBackgroundDrawable(trans)
            trans.startTransition(700)

            val setAnim = AnimatorSet()
            setAnim.play(rotateAnim).with(scaleAnim)

            if (!favorite) {
//                val heartColorAnim = ObjectAnimator.ofInt(fab, "colorFilter",
//                        resources.getColor(R.color.color_favorite))
//                heartColorAnim.duration = 700
//                heartColorAnim.startDelay = 700
                setAnim.playTogether(rotateAnim, scaleAnim)
                fab.setColorFilter(resources.getColor(R.color.gray_dark))

            } else {
                setAnim.playTogether(rotateAnim, scaleAnim)
                fab.setColorFilter(resources.getColor(R.color.white))
            }

            setAnim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    canClickFab = true
                }

                override fun onAnimationStart(animation: Animator?) {
                    canClickFab = false
                }
            })

            setAnim.start()
        }
    }
}