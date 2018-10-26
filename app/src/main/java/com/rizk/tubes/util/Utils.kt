package com.rizk.tubes.util

import android.content.res.Resources
import android.util.Log;

class Log {
    // This is how kotlin supports static methods.
    companion object {
        private var loggable: Boolean = true;

        fun v(tag: String, msg: String) {
            if (loggable) {
                Log.v(tag, msg)
            }
        }
    }
}

class ScreenUtils {
    companion object {
        fun dpToPx(dp: Int): Int = (dp.times(Resources.getSystem().displayMetrics.density)).toInt()
    }
}