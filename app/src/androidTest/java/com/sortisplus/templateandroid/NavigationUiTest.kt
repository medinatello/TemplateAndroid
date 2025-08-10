package com.sortisplus.templateandroid

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sortisplus.core.common.R
import org.junit.Rule
import org.junit.Test

class NavigationUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_to_details_and_back_smoke() {
        // Home visible con botón "Continuar"
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.button_continue), substring = false).assertExists()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.button_continue)).performClick()

        // En Details se ve el texto y botón "Volver"
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.details_screen_message), substring = false).assertExists()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.button_back), substring = false).assertExists()
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.button_back)).performClick()

        // De regreso a Home
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.button_continue), substring = false).assertExists()
    }
}