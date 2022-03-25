package com.uiutilities.airbar.view.animation

/**
 * Интерфейс слушателя жестов эирбара
 */
interface AirBarGestureListener {

    /**
     * Событие долго нажатия
     */
    fun onLongPress()

    /**
     * событие смахивания
     */
    fun onFling()

    /**
     * Событие пееворота
     */
    fun onOverturn()

}