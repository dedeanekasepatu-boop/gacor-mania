package com.example.core.util

import android.util.Log
import com.example.core.common.Constants

/**
 * Internal safe logging utility for Gacor Driver AI.
 * Strictly forbids logging credentials, tokens, or personal identifiers.
 */
object GacorLogger {
    private const val TAG = Constants.TAG

    fun d(message: String) {
        Log.d(TAG, sanitize(message))
    }

    fun i(message: String) {
        Log.i(TAG, sanitize(message))
    }

    fun w(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.w(TAG, sanitize(message), throwable)
        } else {
            Log.w(TAG, sanitize(message))
        }
    }

    fun e(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e(TAG, sanitize(message), throwable)
        } else {
            Log.e(TAG, sanitize(message))
        }
    }

    private fun sanitize(input: String): String {
        // Redact potential tokens, secrets or credentials if accidentally included
        return input.replace(Regex("(?i)(password|token|secret|bearer|authorization)=[^\\s&]+"), "$1=[REDACTED]")
    }
}
