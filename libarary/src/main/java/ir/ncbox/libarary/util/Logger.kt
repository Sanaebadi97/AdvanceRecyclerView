package ir.ncbox.libarary.util

import android.util.Log

class Logger {
    companion object {

        val TAG: String = "ir.ncbox.recyclerView"

        fun i(message: String) {
            Log.i(TAG, message)
        }

        fun w(message: String) {
            Log.w(TAG, message)
        }

        fun e(message: String) {
            Log.e(TAG, message)
        }

    }
}