package com.jakchang.design_system.utils.reflection

// https://issuetracker.google.com/issues/226665301
fun hackMinTabWidth() {
    val clazz = Class.forName("androidx.compose.material.TabRowKt")
    val field = clazz.getDeclaredField("ScrollableTabRowMinimumTabWidth")
    field.isAccessible = true
    field.set(null, 0.0f) // set min width to zero (fit content width)
}
