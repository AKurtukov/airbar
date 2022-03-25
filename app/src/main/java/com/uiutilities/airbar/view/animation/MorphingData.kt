package com.uiutilities.airbar.view.animation

/**
 * Объект с данными о трансформации
 * @property listener - слушатель завершения трансформации
 * @property duration - длительность анимации
 * @property fromWidth - начальная ширина объекта анимации
 * @property toWidth - конечная ширина объекта анимации
 * @property fromColor - стартовый цвет объекта анимации
 * @property toColor - конечный цвет объекта анимации
 * @property fromStrokeColor - стартовый цвет обводки объекта анимации
 * @property toStrokeColor - конечный цвет обводки объекта анимации
 * @property fromCornerRadius - началый радиус закругления объекта анимации
 * @property toCornerRadius - конечный радиус закругления объекта анимации
 * @property padding - отступ бублика "внутрь"
 */
data class MorphingData(
    var listener: OnAnimationEndListener,
    var duration: Long = 0,
    var fromWidth: Int = 0,
    var toWidth: Int = 0,
    var fromColor: Int = 0,
    var toColor: Int = 0,
    var fromStrokeColor: Int = 0,
    var toStrokeColor: Int = 0,
    var fromCornerRadius: Float = 0f,
    var toCornerRadius: Float = 0f,
    var padding: Float = 0f
)
