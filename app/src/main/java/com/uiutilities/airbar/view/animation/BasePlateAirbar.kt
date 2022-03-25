package com.uiutilities.airbar.view.animation

import android.view.View
import com.uiutilities.airbar.view.AirItemView

/**
 * Фундаментная плита Аирбара
 */
interface BasePlateAirbar {

    /**
     * Превращение из начального ного состояния в бублик
     */
    fun morphToBagel()

    /**
     * Превращение в изначальное состояние
     */
    fun morphToComplete()

    /**
     * Получить текущий вью
     * @return - вью
     */
    fun getView(): View

    /**
     * Получить элементы эирбара
     * @return список элементов [AirItemView]
     */
    fun getItems(): List<AirItemView>?

    /**
     * Добавить элементы в эирбар
     * @param items - список элементов [AirItemView]
     */
    fun addListView(items: List<AirItemView>)

    /**
     * Добавить лого вью
     * @param view - вьюха лого
     */
    fun addLogoView(view: View)

    /**
     * Разрешено ли перемещение эирбара
     *
     * @return - true/false
     */
    fun isMoveable(): Boolean
}
