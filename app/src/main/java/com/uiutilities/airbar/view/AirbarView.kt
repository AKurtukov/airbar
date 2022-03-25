package com.uiutilities.airbar.view

import android.content.Context
import android.graphics.Color
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.uiutilities.airbar.R
import com.uiutilities.airbar.view.animation.AirbarAnimation
import com.uiutilities.airbar.view.animation.BasePlateAirbar
import com.uiutilities.airbar.view.animation.BasePlateAirbarImpl
import com.uiutilities.airbar.view.animation.PositionAirbarAnimation

/**
 * View Airbar
 * базовый класс View
 */
class AirbarView : ConstraintLayout {

    private lateinit var attr: AirbarAttr
    private var basePlateAirbar: BasePlateAirbar
    private var airbarAnimation: AirbarAnimation? = null

    /**
    * Добавление логотипа
     * @param view - вью логотипа
    */
    fun addLogoView(view: View) {
        basePlateAirbar.addLogoView(view)
    }

    /**
     * Добавление списка элементов эирбара
     *
     * @param items - список элементов [AirItemData]
     */
    fun addItemListView(items: List<AirItemData>) {
        val list = ArrayList<AirItemView>()
        for (item in items) {
            val airItem = item.toAirItemView()
            list.add(airItem)
        }
        basePlateAirbar.addListView(list)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {

        initAttributes(context, attrs)
        basePlateAirbar = BasePlateAirbarImpl(context, attr)
        airbarAnimation = PositionAirbarAnimation(basePlateAirbar)
        createAirBarLayout()
    }

    /**
    * Скрыть эирбар
    */
    fun dropDown() {
        airbarAnimation?.hide()
    }

    /**
    * Показать эирбар
    */
    fun dropUp() {
        airbarAnimation?.show()
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AirbarView)
        try {
            val paddingBagel = attr.getDimensionPixelSize(R.styleable.AirbarView_paddingBagel, 0)
            val white = Color.WHITE
            val grey = Color.GRAY

            val colorBagel = attr.getColor(R.styleable.AirbarView_colorBagel, white)
            val colorBagelStroke =
                attr.getColor(R.styleable.AirbarView_colorBagelStroke, grey)

            val colorBar = attr.getColor(R.styleable.AirbarView_colorBar, white)

            val bounceBar = attr.getDimensionPixelSize(
                R.styleable.AirbarView_bounceBar,
                context.resources.getDimensionPixelSize(R.dimen.bottom_margin)
            )
            val cornerRadius = attr.getDimensionPixelSize(
                R.styleable.AirbarView_сornerRadius,
                context.resources.getDimensionPixelSize(R.dimen.corner_radius)
            )

            val strokeWidth = attr.getDimensionPixelSize(
                R.styleable.AirbarView_strokeWidth,
                context.resources.getDimensionPixelSize(R.dimen.stroke_width)
            )
            val isMoveable = attr.getBoolean(R.styleable.AirbarView_isMoveable, false)

            this.attr = AirbarAttr(
                paddingBagel = paddingBagel,
                colorBar = colorBar,
                colorBagel = colorBagel,
                colorBagelStroke = colorBagelStroke,
                bounceBar = bounceBar,
                cornerRadius = cornerRadius,
                strokeWidth = strokeWidth,
                isMoveable = isMoveable
            )
        } finally {
            attr.recycle()
        }
    }

    /**
     * Скрытие текста под иконками AirItemViews
     */
    fun hideTextAirItemViews() {
        basePlateAirbar.getItems()?.let { items ->
            items.map { it.titleView?.visibility = GONE }
        }
    }

    /**
     * Добавление AirBar
     */
    private fun createAirBarLayout() {
        val view = basePlateAirbar.getView()

        view.id = ViewCompat.generateViewId() //Invalid ID 0x00000003.

        val airBarLayoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        addView(view, airBarLayoutParams)
        val set = ConstraintSet()
        set.clone(this)
        set.connect(view.id, LEFT, PARENT_ID, LEFT, 0)
        set.connect(view.id, RIGHT, PARENT_ID, RIGHT, 0)
        set.connect(view.id, BOTTOM, PARENT_ID, BOTTOM, attr.bounceBar)
        TransitionManager.beginDelayedTransition(this)
        set.applyTo(this)
    }

    private fun AirItemData.toAirItemView() =
        AirItemView(context).apply {
            titleView?.text = context.getString(titleResId)
            iconView?.setImageResource(iconResId)
            setOnClickListener { clickHandler?.invoke() }
        }
}