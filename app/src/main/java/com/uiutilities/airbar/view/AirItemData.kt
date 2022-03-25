package com.uiutilities.airbar.view

/**
 * Дата класс элемента эирбара
 *
 * @param text - текст элемента эирбара
 * @param icon - ресурсы иконки элемента эирбара
 * @param clickHandler - обработчик нажатий на элемент эирбара
 */
data class AirItemData(
    val titleResId: Int,
    val iconResId: Int,
    var clickHandler: (() -> Unit)?
)