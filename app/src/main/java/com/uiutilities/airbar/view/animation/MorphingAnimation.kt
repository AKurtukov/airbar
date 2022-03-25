package com.uiutilities.airbar.view.animation

import android.animation.*
import android.view.View

/**
 * Morphing (Морфинг) - это специальный эффект в движущихся изображениях и анимации,
 * который изменяет (или трансформирует) одно изображение или форму в другое посредством плавного перехода.
 * @property view - вью, над которым производим действия
 * @property drawable над которым производим действия
 * @property data - данные о параметрах трансформации объекта [MorphingData]
 */
internal class MorphingAnimation(
    private val view: View,
    private val drawable: StrokeGradientDrawable,
    private val data: MorphingData
) {

    /**
    * Слушатель окончания анимации
    */
    private var listener: OnAnimationEndListener? = null

    /**
     * Запуск анимации
     */
    fun start() {
        val widthAnimation = ValueAnimator.ofInt(data.fromWidth, data.toWidth)
        val gradientDrawable = drawable.gradientDrawable
        widthAnimation.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val leftOffset: Int
            val rightOffset: Int
            val padding: Int
            if (data.fromWidth > data.toWidth) {
                leftOffset = (data.fromWidth - value) / 2
                rightOffset = data.fromWidth - leftOffset
                padding = (data.padding * animation.animatedFraction).toInt()
            } else {
                leftOffset = (data.toWidth - value) / 2
                rightOffset = data.toWidth - leftOffset
                padding = (data.padding - data.padding * animation.animatedFraction).toInt()
            }
            gradientDrawable
                .setBounds(
                    leftOffset + padding,
                    padding,
                    rightOffset - padding,
                    view.height - padding
                )
        }
        val bgColorAnimation =
            ObjectAnimator.ofInt(gradientDrawable, COLOR, data.fromColor, data.toColor)
        bgColorAnimation.setEvaluator(ArgbEvaluator())
        val strokeColorAnimation =
            ObjectAnimator.ofInt(drawable, STROKE_COLOR, data.fromStrokeColor, data.toStrokeColor)
        strokeColorAnimation.setEvaluator(ArgbEvaluator())
        val cornerAnimation = ObjectAnimator.ofFloat(
            gradientDrawable,
            CORNER_RADIUS,
            data.fromCornerRadius,
            data.toCornerRadius
        )
        val animatorSet = AnimatorSet()
        animatorSet.duration = data.duration
        animatorSet.playTogether(
            widthAnimation,
            bgColorAnimation,
            strokeColorAnimation,
            cornerAnimation
        )
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                listener?.onAnimationEnd()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animatorSet.start()
    }

    companion object {

        /**
         * Скорость анимации сворачивания/разворачивания
         */
        const val ANIMATION_DURATION = 400L

        /**
        * Константа для обозначения цвета обводки элемента
        */
        const val STROKE_COLOR = "strokeColor"

        /**
         * Константа для обозначения основного цвета элемента
         */
        const val COLOR = "color"

        /**
         * Константа для обозначения радиуса углов элемента
         */
        const val CORNER_RADIUS = "cornerRadius"
    }
}