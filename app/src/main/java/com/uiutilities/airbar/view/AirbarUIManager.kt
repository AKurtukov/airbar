package com.uiutilities.airbar.view

import com.uiutilities.airbar.R

/**
 * UI менеджер для аирбар
 */
object AirbarUIManager {

    enum class AirItems(
        val item: AirItemData
    ) {
        INFO_ACTION(
            AirItemData(
                R.string.deposit_detail_action_info,
                R.drawable.ic_action_about_deposit_40dp,
                null
            )
        ),
        ADD_ACTION(
            AirItemData(
                R.string.deposit_detail_action_add,
                R.drawable.ic_action_add_to_deposit_40dp,
                null
            )
        ),
        WITHDRAW_ACTION(
            AirItemData(
                R.string.deposit_detail_action_withdraw,
                R.drawable.ic_action_withdraw_deposit_40dp,
                null
            )
        ),
        TOOLBAR_ACTION(
            AirItemData(
                R.string.deposit_detail_action_settings,
                R.drawable.ic_settings_deposit_toolbar_21dp,
                null
            )
        )
    }

    enum class AirbarBackgroundColors(val color: Int) {
        BASIC_COLOR(R.color.cpb_grey),
        ADDITIONAL_COLOR(R.color.cpb_blue),
        SELECTED_COLOR(R.color.cpb_blue_dark),
        ACCENT_COLOR(R.color.cpb_red),
        ACCENT_DISABLED_COLOR(R.color.cpb_red_dark),
        PRESSED_COLOR(R.color.cpb_green),
        EXTRA_COLOR(R.color.cpb_green_dark),
        ATTENTION_COLOR(R.color.cpb_white),
        SYSTEM_CLEARE_COLOR(R.color.cpb_grey),
        CARDS_COLOR(R.color.cpb_blue),
        MODAL_VIEW_COLOR(R.color.cpb_blue_dark),
        SHIMMERS_COLOR(R.color.cpb_red)
    }

}