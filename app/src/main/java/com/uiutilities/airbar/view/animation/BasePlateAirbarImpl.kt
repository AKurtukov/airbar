package com.uiutilities.airbar.view.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.transition.TransitionManager
import android.util.StateSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.uiutilities.airbar.view.AirItemView
import com.uiutilities.airbar.view.AirbarAttr

/**
 * Реализация нижнего слоя эирбара. Представляет из себя наследника [ConstraintLayout]
 * @param context - контекст
 * @property attr - атрибуты эирбара [AirbarAttr]
 */
@SuppressLint("ViewConstructor")
class BasePlateAirbarImpl(context: Context, private val attr: AirbarAttr) :
    ConstraintLayout(context),
    BasePlateAirbar {

    /**
     * Бэкграунд с обводкой
     */
    private lateinit var background: StrokeGradientDrawable

    /**
     * Состояние
     */
    private lateinit var stateDrawable: StateListDrawable

    /**
     * Текущее состояние [State]
     * */
    private var state: State = State.COMPLETE

    /**
     * Список элементов эирбара [AirItem]
     */
    private var items: List<AirItemView>? = null

    /**
     * Лэйаут, на котором располагаются активные элементы эирбара
     */
    private val itemsLayout: LinearLayout

    /**
     * Лэйаут на котором располагается лого.
     * Появляется при сворачивании эирбара в бублик при перетаскивании
     */
    private val logoLayout: LinearLayout

    /**
     * Разрешено ли перемещение эирбара
     */
    private var isMoveable: Boolean = false

    /**
     * Класс описывает возможные состояния нижнего слоя эирбара
     * */
    private enum class State {
        /**
         * Состояние бублика
         */
        BAGEL,

        /**
         * Конечное состояние(или развернутое), когда видны элементы
         */
        COMPLETE
    }

    /**
     * Слушатель анимации сворачивания в бублик
     */
    private val bagelStateListener: OnAnimationEndListener = object : OnAnimationEndListener {
        override fun onAnimationEnd() {
            state = State.BAGEL
        }
    }

    /**
     * Слушатель анимации трансформации в конечное состояние
     */
    private val completeStateListener: OnAnimationEndListener = object : OnAnimationEndListener {
        override fun onAnimationEnd() {
            state = State.COMPLETE
        }
    }


    init {
        initStateDrawable()
        setBackground(stateDrawable)
        itemsLayout = createCentreLayout(LinearLayout(context))
        logoLayout = createCentreLayout(LinearLayout(context))
        logoLayout.visibility = INVISIBLE
        isMoveable = attr.isMoveable
    }

    override fun setBackgroundColor(color: Int) {
        background.gradientDrawable.setColor(color)
    }

    override fun drawableStateChanged() {
        if (state == State.COMPLETE) {
            initStateDrawable()
            setBackground(stateDrawable)
        }
        if (state != State.BAGEL) {
            super.drawableStateChanged()
        }
    }

    override fun morphToBagel() {
        val data = MorphingData(
            listener = bagelStateListener,
            duration = MorphingAnimation.ANIMATION_DURATION,
            fromWidth = width,
            toWidth = height,
            fromColor = attr.colorBar,
            toColor = attr.colorBagel,
            fromStrokeColor = attr.colorBar,
            toStrokeColor = attr.colorBagelStroke,
            fromCornerRadius = attr.cornerRadius.toFloat(),
            toCornerRadius = height.toFloat(),
            padding = attr.paddingBagel.toFloat()
        )
        val animation = MorphingAnimation(this, background, data)
        animation.start()

        viewAnimationVisible(logoLayout)
        viewAnimationInvisible(itemsLayout)
    }

    override fun morphToComplete() {
        val data = MorphingData(
            listener = completeStateListener,
            duration = MorphingAnimation.ANIMATION_DURATION,
            fromWidth = width,
            toWidth = width,
            fromColor = attr.colorBagel,
            toColor = attr.colorBar,
            toStrokeColor = attr.colorBar,
            fromCornerRadius = attr.cornerRadius.toFloat(),
            toCornerRadius = attr.cornerRadius.toFloat(),
        )
        val animation = MorphingAnimation(this, background, data)
        animation.start()

        viewAnimationVisible(itemsLayout)
        viewAnimationInvisible(logoLayout)
    }

    override fun getView(): View = this

    override fun getItems(): List<AirItemView>? = items

    override fun addListView(items: List<AirItemView>) {
        this.items = items
        for (item in items) itemsLayout.addView(item)
    }

    override fun addLogoView(view: View) {
        logoLayout.addView(view)
    }

    override fun isMoveable(): Boolean = isMoveable

    /**
     * Метод используется для создания дочерних элементов эирбара
     * @param view - лэйаут с дочерним элементом
     */
    private fun createCentreLayout(view: LinearLayout): LinearLayout {
        view.id = ViewCompat.generateViewId()
        val layoutLayoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(view, layoutLayoutParams)
        val set = ConstraintSet()
        set.clone(this)

        set.connect(view.id, LEFT, PARENT_ID, LEFT, 0)
        set.connect(view.id, RIGHT, PARENT_ID, RIGHT, 0)
        set.connect(view.id, BOTTOM, PARENT_ID, BOTTOM, 0)
        set.connect(view.id, TOP, PARENT_ID, TOP, 0)
        TransitionManager.beginDelayedTransition(this)
        set.applyTo(this)
        return view
    }

    /**
     * Инициализация состояний
     */
    private fun initStateDrawable() {
        val colorNormal = attr.colorBar
        val colorPressed = attr.colorBar
        val colorFocused = attr.colorBar
        val colorDisabled = attr.colorBar
        background = createDrawable(colorNormal)
        val drawableDisabled = createDrawable(colorDisabled)
        val drawableFocused = createDrawable(colorFocused)
        val drawablePressed = createDrawable(colorPressed)
        stateDrawable = StateListDrawable()
        stateDrawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            drawablePressed.gradientDrawable
        )
        stateDrawable.addState(
            intArrayOf(android.R.attr.state_focused),
            drawableFocused.gradientDrawable
        )
        stateDrawable.addState(
            intArrayOf(-android.R.attr.state_enabled),
            drawableDisabled.gradientDrawable
        )
        stateDrawable.addState(StateSet.WILD_CARD, background.gradientDrawable)
    }

    /**
     * Вспомогательный метод для initStateDrawable
     * @return drawable для различных состояний эирбара
     * */
    private fun createDrawable(color: Int): StrokeGradientDrawable {
        val drawable = GradientDrawable()
        drawable.setColor(color)
        drawable.cornerRadius = attr.cornerRadius.toFloat()
        val strokeGradientDrawable = StrokeGradientDrawable(drawable)
        strokeGradientDrawable.strokeColor = color
        strokeGradientDrawable.strokeWidth = attr.strokeWidth
        return strokeGradientDrawable
    }

    /**
     * Анимация появления View через прозрачность
     * @param view - вью
     */
    private fun viewAnimationInvisible(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.INVISIBLE
                }
            })
    }

    /**
     * Анимация исчезания View через прозрачность
     * @param view - вью
     */
    private fun viewAnimationVisible(view: View) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null)
        }
    }


    companion object {

        /**
         * Скорость анимации сворачивания/разворачивания
         */
        const val ANIMATION_DURATION = 100L
    }
}