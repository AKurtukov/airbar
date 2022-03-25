package com.uiutilities.airbar.view.animation

import android.graphics.drawable.GradientDrawable

/**
 * Класс описывает бэкграунд c обводкой эирбара
 * @property gradientDrawable - начальный дровабл бэкграунда
 */
class StrokeGradientDrawable(val gradientDrawable: GradientDrawable) {
    private var mStrokeWidth = 0
    private var mStrokeColor = 0
    var strokeWidth: Int
        get() = mStrokeWidth
        set(strokeWidth) {
            mStrokeWidth = strokeWidth
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }
    var strokeColor: Int
        get() = mStrokeColor
        set(strokeColor) {
            mStrokeColor = strokeColor
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }
}