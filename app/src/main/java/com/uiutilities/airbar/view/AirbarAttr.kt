package com.uiutilities.airbar.view

/**
 * Объект с доступными атрибутами эирбара
 * @property colorBar - основной цвет
 * @property colorBagel - цвет бублика
 * @property colorBagelStroke - цвет обводки бублика
 * @property bounceBar - отступ эирбара от нижнего края экрана
 * @property paddingBagel - паддинг бубика
 * @property cornerRadius - радиус закругления
 * @property strokeWidth - ширина обводки
 * @property isMoveable - разрешено ли перемещение эирбара
 */
data class AirbarAttr(
    val colorBar: Int,
    val colorBagel: Int,
    val colorBagelStroke: Int,
    val bounceBar: Int,
    val paddingBagel: Int,
    val cornerRadius: Int,
    val strokeWidth: Int,
    val isMoveable: Boolean
)
