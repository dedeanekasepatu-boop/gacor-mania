package com.example.core.util

import com.example.core.common.Constants.PLACEHOLDER_EMPTY
import java.util.Locale

object Formatters {

    fun formatCoordinate(value: Double?): String {
        return if (value != null) {
            String.format(Locale.US, "%.6f", value)
        } else {
            PLACEHOLDER_EMPTY
        }
    }

    fun formatAccuracy(accuracyMeters: Float?): String {
        return if (accuracyMeters != null && accuracyMeters > 0) {
            String.format(Locale.US, "± %.1f m", accuracyMeters)
        } else {
            PLACEHOLDER_EMPTY
        }
    }

    fun formatSpeed(speedMps: Float?): String {
        return if (speedMps != null && speedMps >= 0) {
            val speedKmh = speedMps * 3.6f
            String.format(Locale.US, "%.1f km/h", speedKmh)
        } else {
            PLACEHOLDER_EMPTY
        }
    }

    fun formatLatency(latencyMs: Long?): String {
        return if (latencyMs != null && latencyMs >= 0) {
            "$latencyMs ms"
        } else {
            PLACEHOLDER_EMPTY
        }
    }
}
