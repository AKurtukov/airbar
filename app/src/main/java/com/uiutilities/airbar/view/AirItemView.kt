package com.uiutilities.airbar.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.uiutilities.airbar.R

/**
 * View элемента эирбара
 *
 * @param context - контекст
 * @param attrs - атрибуты эирбара [AirbarAttr]
 */
@SuppressLint("ViewConstructor")
class AirItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    var titleView: TextView? = null
    var iconView: ImageView? = null

    init {
        View.inflate(context, R.layout.air_item, this).apply {
            titleView = findViewById(R.id.title)
            iconView = findViewById(R.id.icon)
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener(listener)
        super.setClickable(false)
    }

    override fun setOnLongClickListener(listener: OnLongClickListener?) {}
}