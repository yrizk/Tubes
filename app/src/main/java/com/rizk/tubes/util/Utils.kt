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

class ViewUtils {
    companion object {
        fun dpToPx(dp: Int): Int = (dp.times(Resources.getSystem().displayMetrics.density)).toInt()

        // Assumes the input is a square matrix
        /**
         *
         */
        fun twoDimensionToOneDimension(i : Int, j : Int, width: Int): Int =  i * width + j

        /**
         * x the one dimension
         * size: the width (assumes that the grid is square)
         */
        fun oneDimensionToTwoDimension(x : Int, size: Int) : Pair<Int, Int> = Pair(x % size, x / size)
    }
}