package com.uiutilities.airbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.uiutilities.airbar.view.AirItemData
import com.uiutilities.airbar.view.AirbarUIManager
import com.uiutilities.airbar.view.AirbarView

/**
 * Главный экран демонстрации AirbarView
 */
class MainActivity : AppCompatActivity() {

    private var explosionView: AirbarView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        explosionView = findViewById(R.id.explosionView)


        // Добавляем секции
        explosionView?.addItemListView(getItemList())

        // Добавляем лого
        explosionView?.addLogoView(
            LayoutInflater.from(this).inflate(R.layout.logo_item, null) as View
        )
    }

    /**
     * Получить список секций аирбара
     */
    private fun getItemList(): ArrayList<AirItemData> {
        val items = ArrayList<AirItemData>()

        val testItemFirst = AirbarUIManager.AirItems.INFO_ACTION.item
        testItemFirst.clickHandler = {
            //TODO клик
        }
        val testItem2 = AirbarUIManager.AirItems.ADD_ACTION.item
        testItem2.clickHandler = {
            //TODO клик
        }

        items.add(testItemFirst)
        items.add(testItem2)

        return items
    }
}