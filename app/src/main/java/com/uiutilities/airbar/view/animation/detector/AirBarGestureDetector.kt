package com.uiutilities.airbar.view.animation.detector

import android.content.Context
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import com.uiutilities.airbar.view.AirItemView

/**
 * Детектор жестов эирбара, наследник стандартного GestureDetector.
 * Расширен для возможности работы с элементом эирбара [AirItem]
 */
class AirBarGestureDetector : GestureDetector {

    /**
     * Слушатель жестов
     */
    var listener: GestureListener

    /**
     * Конструтктор класса
     * @param context - контекст
     * @param listener - служатель жестов
     */
    internal constructor(context: Context?, listener: GestureListener) : super(context, listener) {
        this.listener = listener
    }

    /**
     * Конструтктор класса
     * @param context - контекст
     * @param listener - служатель жестов
     * @param handler - хэндлер
     */
    internal constructor(context: Context?, listener: GestureListener, handler: Handler?) : super(
        context,
        listener,
        handler
    ) {
        this.listener = listener
    }

    /**
     * Конструтктор класса
     * @param context - контекст
     * @param listener - служатель жестов
     * @param handler - хэндлер
     * @param unused - не используется (в родителе причем тоже не используется)
     */
    constructor(
        context: Context?,
        listener: GestureListener,
        handler: Handler?,
        unused: Boolean
    ) : super(context, listener, handler, unused) {
        this.listener = listener
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        listener.item = null
        return super.onTouchEvent(ev)
    }

    /**
     * Обработчик касания
     * @param event - событие
     * @param item - элемент эирбара
     */
    fun onTouchEvent(event: MotionEvent?, item: AirItemView?): Boolean {
        item?.let { listener.item = it }
        return super.onTouchEvent(event)
    }
}