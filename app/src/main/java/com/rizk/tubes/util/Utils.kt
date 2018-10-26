package com.rizk.tubes.util

import android.util.Log;

public final class Log {
    // This is how kotlin supports static methods.
    companion object {
        private var loggable: Boolean = true;

        fun setLoggable(on: Boolean) {
            loggable = on
        }

        fun v(tag: String, msg: String) {
            if (loggable) {
                Log.v(tag, msg)
            }
        }
    }
}