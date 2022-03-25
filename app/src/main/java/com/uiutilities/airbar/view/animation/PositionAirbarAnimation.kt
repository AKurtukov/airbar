package com.uiutilities.airbar.view.animation

import android.annotation.SuppressLint
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.uiutilities.airbar.view.AirItemView
import com.uiutilities.airbar.view.animation.detector.AirBarGestureDetector
import com.uiutilities.airbar.view.animation.detector.GestureListener


/**
 * Анимация логотипа - сдвиг по клику и возврат на свое место
 */
class PositionAirbarAnimation(private val basePlateAirbar: BasePlateAirbar) : AirBarGestureListener,
    AirbarAnimation {

    /**
     * Вью нижнего слоя эирбара [BasePlateAirbar]
     */
    private val animatedView: View = basePlateAirbar.getView()

    /**
     * Детектор жестов
     */
    private val detector: AirBarGestureDetector =
        AirBarGestureDetector(animatedView.context, GestureListener(this))

    /**
     * Слушатель общего состояния лэйаута
     */
    private val globalLayoutListener = OnGlobalLayoutListener {
        xAnimation = createSpringAnimation(SpringAnimation.X, animatedView.x)
        yAnimation = createSpringAnimation(SpringAnimation.Y, animatedView.y)
        if (animatedViewX == 0f && animatedViewY == 0f) {
            animatedViewX = animatedView.x
            animatedViewY = animatedView.y

            val size = Point()
            basePlateAirbar.getView().display?.getSize(size)
            height = size.y.toFloat()
            width = size.x.toFloat()
        }
    }

    //событие долгий клик
    private var onLongClickListener = false

    /**
     * Анимация "пружинки" по оси x
     * */
    private var xAnimation: SpringAnimation? = null

    /**
     * Анимация "пружинки" по оси y
     * */
    private var yAnimation: SpringAnimation? = null

    /**
     * Дельта между координатой view и координатой касания по оси x
     */
    private var dX = 0f

    /**
     * Дельта между координатой view и координатой касания по оси y
     */
    private var dY = 0f

    /**
     * Финальная координата вью по оси x, после завершения анимации
     */
    private var animatedViewX = 0f

    /**
     * Финальная координата вью по оси y, после завершения анимации
     */
    private var animatedViewY = 0f

    /**
     * Высота view базового слоя эирбара [BasePlateAirbar]
     */
    private var height = 0f

    /**
     * Ширина view базового слоя эирбара [BasePlateAirbar]
     */
    private var width = 0f

    /**
     * Текущая позиция [Position]
     * */
    private var position = Position.CENTER

    /**
     * Текущее состояние "смахивания" [FlingPosition]
     */
    private var flingPosition = FlingPosition.HIDDEN

    /**
     * Класс описывает возможные позиции эирбара на экране при перемещении.
     * На основание позиции принимается решение, куда прилипает эирбар после перемещения
     */
    private enum class Position {
        LEFT, CENTER, RIGHT
    }

    /**
     * Класс описывает состояние эирбара для "смахивания" (скрыт/раскрыт)
     *
     */
    private enum class FlingPosition {
        SHOWN, HIDDEN
    }

    /**
     * На основании этой дельты реализуется эффект "подтягивания" эирбара к пальцу при
     * старте анимации сворачивания в бублик
     */
    private var animationDiff = 0f

    /**
     * Все величины в пикселях
     */
    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = OnTouchListener { view, event ->
        var item: AirItemView? = null
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
                xAnimation?.cancel()
                yAnimation?.cancel()

                item = getAirItem(view.left, view.right, event.rawX.toInt())
                animationDiff = getAmendment(view.left, view.right, event.rawX.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
                if (onLongClickListener) {
                    animatedView.animate().x(
                        event.rawX + dX - animationDiff
                    ).y(event.rawY + dY).setDuration(0).start()
                    when (getPositionFromRawX(event.rawX)) {
                        Position.LEFT -> {
                            xAnimation = createSpringAnimation(
                                SpringAnimation.X,
                                0f - basePlateAirbar.getView().width.toFloat() + 100 // TODO Убрать 100 (рассчитывать это смещение)
                            )
                            yAnimation = createSpringAnimation(SpringAnimation.Y, event.rawY + dY)
                            position = Position.LEFT
                        }
                        Position.CENTER -> {
                            xAnimation = createSpringAnimation(SpringAnimation.X, animatedViewX)
                            yAnimation = createSpringAnimation(SpringAnimation.Y, animatedViewY)
                            position = Position.CENTER
                        }
                        Position.RIGHT -> {
                            xAnimation = createSpringAnimation(SpringAnimation.X, width - 100) // TODO Убрать 100 (рассчитывать это смещение)
                            yAnimation = createSpringAnimation(SpringAnimation.Y, event.rawY + dY)
                            position = Position.RIGHT
                        }
                    }
                    flingPosition = FlingPosition.HIDDEN
                }
            }
            MotionEvent.ACTION_UP -> {
                if (onLongClickListener) {
                    animatedView.clearAnimation()
                    createAnimationFallingListener(animatedView)
                    yAnimation?.start()
                    xAnimation?.start()
                    onLongClickListener = false
                }
            }
        }

        return@OnTouchListener detector.onTouchEvent(event, item)
    }

    /**
     * Определение позиции эирбара на экране при перетаскивании.
     * Условно экран делится на 3 части, далее в зависимости от того, в какой части при
     * перетаскивании находится эирбар, вычисляеся его позиция
     * @param rawX - координата касания по оси x
     * @return [Position]
     */
    private fun getPositionFromRawX(rawX: Float): Position {
        if (rawX < width / 3) return Position.LEFT
        if (rawX > width / 3 && rawX < width / 3 * 2) return Position.CENTER
        if (rawX > width / 3 * 2) return Position.RIGHT
        return Position.CENTER
    }

    init {
        animatedView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        animatedView.setOnTouchListener(touchListener)
    }

    override fun onLongPress() {
        if (basePlateAirbar.isMoveable()) {
            onLongClickListener = true
            //Клубочек
            basePlateAirbar.morphToBagel()
            biasX()
            basePlateAirbar.getView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }


    override fun onFling() {

        when (position) {
            Position.LEFT -> {
                if (flingPosition == FlingPosition.HIDDEN) {
                    basePlateAirbar.getView()
                        .animate()
                        .setDuration(MorphingAnimation.ANIMATION_DURATION)
                        .x(basePlateAirbar.getView().x + basePlateAirbar.getView().width.toFloat() - 100)
                        .y(basePlateAirbar.getView().y)
                        .start()
                    flingPosition = FlingPosition.SHOWN
                } else {
                    basePlateAirbar.getView()
                        .animate()
                        .setDuration(MorphingAnimation.ANIMATION_DURATION)
                        .x(basePlateAirbar.getView().x - basePlateAirbar.getView().width.toFloat() + 100)
                        .y(basePlateAirbar.getView().y)
                        .start()
                    flingPosition = FlingPosition.HIDDEN
                }
            }
            Position.CENTER -> {
//                ((right - left) / 2 - (viewX - left)).toFloat()
            }
            Position.RIGHT -> {
                if (flingPosition == FlingPosition.SHOWN) {
                    basePlateAirbar.getView()
                        .animate()
                        .setDuration(MorphingAnimation.ANIMATION_DURATION)
                        .x(width - 100)
                        .y(basePlateAirbar.getView().y)
                        .start()
                    flingPosition = FlingPosition.HIDDEN
                } else {
                    basePlateAirbar.getView()
                        .animate()
                        .setDuration(MorphingAnimation.ANIMATION_DURATION)
                        .x(basePlateAirbar.getView().x - basePlateAirbar.getView().width.toFloat() + 100)
                        .y(basePlateAirbar.getView().y)
                        .start()
                    flingPosition = FlingPosition.SHOWN
                }
            }
        }
    }

    override fun hide() {
        if (position == Position.CENTER) {
            val view = basePlateAirbar.getView()
            view.animate()
                .setDuration(ANIMATION_DURATION)
                .x(basePlateAirbar.getView().x)
                .y((basePlateAirbar.getView().parent as View).height.toFloat())
                .start()
        }
    }

    override fun show() {
        if (position == Position.CENTER) {
            val view = basePlateAirbar.getView()
            view.animate()
                .setDuration(ANIMATION_DURATION)
                .x(basePlateAirbar.getView().x)
                .y(animatedViewY)
                .start()
        }
    }

    override fun onOverturn() {
        dropRotation()
    }

    /**
     * Смягчение перемещения для бублика
     */
    private fun biasX() {
        basePlateAirbar.getView()
            .animate()
            .setDuration(BasePlateAirbarImpl.ANIMATION_DURATION)
            .x(basePlateAirbar.getView().x - animationDiff)
            .y(basePlateAirbar.getView().y)
            .start()
    }

    /**
     * Метод на вход получает границы эирбара и X координату касания по экрану
     * На основе этого вычисляет по какому элементу производилось касание
     * @param left - значение границы слева
     * @param right - значение границы справа
     * @param eventX - Х координата события касания
     */
    private fun getAirItem(left: Int, right: Int, eventX: Int): AirItemView? {
        val items = basePlateAirbar.getItems()
        if (items.isNullOrEmpty()) {
            return null
        }
        if (items.size == 1) {
            return items[0]
        }
        val index = (eventX / ((right - left) / items.size)) - 1
        return if (-1 < index && index < items.size) items[index] else null
    }

    /**
     * При трансформации эирбара в бублик перемещает бублик к пальцу
     * @param left - значение границы слева
     * @param right - значение границы справа
     * @param eventX - Х координата события касания
     */
    private fun getAmendment(left: Int, right: Int, eventX: Int): Float {
        return when (position) {
            Position.LEFT -> {
                when (flingPosition) {
                    FlingPosition.SHOWN -> ((right - left) / 2 - eventX).toFloat()
                    FlingPosition.HIDDEN -> -((right - left) / 2).toFloat()
                }
            }
            Position.CENTER -> {
                ((right - left) / 2 - (eventX - left)).toFloat()
            }
            Position.RIGHT -> {
                when (flingPosition) {
                    FlingPosition.SHOWN -> ((right - left) / 2 - (eventX - 2*left)).toFloat()
                    FlingPosition.HIDDEN -> ((right - left) / 2).toFloat()
                }

            }
        }
    }

    /**
     * Метод создает анимацию падения бублика с превращением обратно в эирбар
     * @param view - вью объект анимации
     */
    private fun createAnimationFallingListener(view: View) {

        val animationFallingListener: Animation.AnimationListener = object :
            Animation.AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                when (position) {
                    Position.LEFT -> {
                    }
                    Position.CENTER -> {
                    }
                    Position.RIGHT -> {
                    }
                }
                basePlateAirbar.morphToComplete()
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {}
        }

        val anim: Animation = TranslateAnimation(0F, 0F, 0F, 0F)
        anim.setAnimationListener(animationFallingListener)
        view.startAnimation(anim)
    }


    /**
    * Метод создает объект анимации "пружины"
     * @param property - свойства объекта анимации
     * @param finalPosition - финальная позиция элемента по завершении анимации
    */
    private fun createSpringAnimation(
        property: ViewProperty,
        finalPosition: Float
    ): SpringAnimation = SpringAnimation(animatedView, property).apply {
        spring = SpringForce(finalPosition).apply {
            this.stiffness = SpringForce.STIFFNESS_MEDIUM
            this.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY //DAMPING_RATIO_LOW_BOUNCY
        }
    }

    /**
    * Метод стартует анимацию переворота эирбара
    */
    fun dropRotation() {
        basePlateAirbar.getView().animate()
            .setDuration(ANIMATION_DURATION_ROTATION)
            .rotation(if (basePlateAirbar.getView().rotation == ROTATION_0) ROTATION_180 else ROTATION_0)
            .start()
    }

    companion object {
        private const val ANIMATION_DURATION = 200L
        private const val ANIMATION_DURATION_ROTATION = 400L
        private const val ROTATION_180 = -180f
        private const val ROTATION_0 = 0f
    }
}