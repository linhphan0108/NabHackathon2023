package com.example.nabhackathon2023.base.util

import android.content.res.Resources

fun Int.dpToPx(): Float {
    return (this * Resources.getSystem().displayMetrics.density)
}

fun Int.pxToDp(): Float {
    return (this / Resources.getSystem().displayMetrics.density)
}

fun Int.spToPx(): Float {
    return (this * Resources.getSystem().displayMetrics.scaledDensity)
}
