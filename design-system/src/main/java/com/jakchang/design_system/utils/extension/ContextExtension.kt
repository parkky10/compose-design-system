package com.jakchang.design_system.utils.extension

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

internal fun Context.getPxToDp(value: Float): Float {
    val density = this.resources.displayMetrics.density
    return value / density
}

internal fun Context.getDpToPx(value: Float): Float {
    val density = this.resources.displayMetrics.density
    return value * density
}

internal fun Context.getDisplayWidth(): Int {
    val dism = DisplayMetrics()
    if (this is Activity) {
        this.windowManager.defaultDisplay.getMetrics(dism)
    } else {
        val windowManager: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(dism)
    }
    return dism.widthPixels
}
