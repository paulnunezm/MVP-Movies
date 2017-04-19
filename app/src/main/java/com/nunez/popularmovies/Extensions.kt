package com.nunez.popularmovies

import android.os.Build

/**
 * Created by paulnunez on 4/18/17.
 */

fun runOnLollipopOrGreater(handler: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        handler()
    }
}