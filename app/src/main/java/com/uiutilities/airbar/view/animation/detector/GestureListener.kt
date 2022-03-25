package com.uiutilities.airbar.view.animation.detector

import android.view.GestureDetector
import android.view.MotionEvent
import com.uiutilities.airbar.view.AirItemView
import com.uiutilities.airbar.view.animation.AirBarGestureListener

/**
 * Слушатьель жестов, реализует стандартный интерфейс [GestureDetector.SimpleOnGestureListener]
 * @property listener - слушатель жестов
 */
class GestureListener(private val listener: AirBarGestureListener) :
    GestureDetector.SimpleOnGestureListener() {

    /**
     * Эелемент эирбара
     */
    var item: AirItemView? = null

    override fun onDown(event: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        item?.isClickable = true
        item?.onTouchEvent(MotionEvent.obtain(100, 100, MotionEvent.ACTION_DOWN, 1f, 1f, 0))
        item?.onTouchEvent(MotionEvent.obtain(100, 100, MotionEvent.ACTION_UP, 1f, 1f, 0))
        item?.isClickable = false
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        listener.onLongPress()
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(
        e1: MotionEvent, e2: MotionEvent,
        distanceX: Float, distanceY: Float
    ): Boolean {
        return true
    }

    override fun onFling(
        event1: MotionEvent, event2: MotionEvent,
        velocityX: Float, velocityY: Float
    ): Boolean {
        listener.onFling()
        return true
    }
}