package com.example.ml

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.abs

enum class ImageQualityIssue(val title: String) {
    TOO_SMALL("Resolution Too Small"),
    TOO_DARK("Insufficient Lighting"),
    TOO_BRIGHT("Severe Glare / Overexposed"),
    TOO_BLURRY("Blurry / Out of Focus")
}

data class ImageQualityResult(
    val isAcceptable: Boolean,
    val warningMessage: String? = null,
    val issueType: ImageQualityIssue? = null
)

object ImageQualityChecker {

    fun checkQuality(bitmap: Bitmap): ImageQualityResult {
        if (bitmap.width < 120 || bitmap.height < 120) {
            return ImageQualityResult(
                isAcceptable = false,
                warningMessage = "Image resolution is too small (${bitmap.width}x${bitmap.height}). Please take a closer photo of the leaf.",
                issueType = ImageQualityIssue.TOO_SMALL
            )
        }

        // Sample up to 64x64 grid for quick, lightweight analysis
        val sampleW = 64
        val sampleH = 64
        val scaled = Bitmap.createScaledBitmap(bitmap, sampleW, sampleH, false)
        val pixels = IntArray(sampleW * sampleH)
        scaled.getPixels(pixels, 0, sampleW, 0, 0, sampleW, sampleH)

        var totalLuminance = 0.0
        var overexposedPixels = 0
        var totalGradient = 0.0

        for (y in 0 until sampleH) {
            for (x in 0 until sampleW) {
                val p = pixels[y * sampleW + x]
                val r = Color.red(p)
                val g = Color.green(p)
                val b = Color.blue(p)
                val lum = 0.299 * r + 0.587 * g + 0.114 * b
                totalLuminance += lum

                if (lum > 240) {
                    overexposedPixels++
                }

                if (x < sampleW - 1 && y < sampleH - 1) {
                    val rightP = pixels[y * sampleW + (x + 1)]
                    val downP = pixels[(y + 1) * sampleW + x]
                    val rightLum = 0.299 * Color.red(rightP) + 0.587 * Color.green(rightP) + 0.114 * Color.blue(rightP)
                    val downLum = 0.299 * Color.red(downP) + 0.587 * Color.green(downP) + 0.114 * Color.blue(downP)
                    totalGradient += (abs(lum - rightLum) + abs(lum - downLum))
                }
            }
        }

        val totalPixels = (sampleW * sampleH).toDouble()
        val avgLuminance = totalLuminance / totalPixels
        val overexposedRatio = overexposedPixels / totalPixels
        val avgGradient = totalGradient / ((sampleW - 1) * (sampleH - 1))

        if (avgLuminance < 30.0) {
            return ImageQualityResult(
                isAcceptable = false,
                warningMessage = "Image is too dark (average brightness is very low). Use better lighting or move near a window.",
                issueType = ImageQualityIssue.TOO_DARK
            )
        }

        if (avgLuminance > 225.0 || overexposedRatio > 0.45) {
            return ImageQualityResult(
                isAcceptable = false,
                warningMessage = "Image has severe glare or is overexposed. Avoid harsh direct flash and direct reflections.",
                issueType = ImageQualityIssue.TOO_BRIGHT
            )
        }

        if (avgGradient < 3.2) {
            return ImageQualityResult(
                isAcceptable = false,
                warningMessage = "Image appears blurry or out of focus. Keep the phone steady and tap to focus on the leaf.",
                issueType = ImageQualityIssue.TOO_BLURRY
            )
        }

        return ImageQualityResult(isAcceptable = true)
    }
}
