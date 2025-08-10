package com.sortisplus.templateandroid

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NavigationUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_to_details_and_back_smoke() {
        // Home visible con botón "Continuar"
        composeRule.onNodeWithText("Continuar", substring = false).assertExists()
        composeRule.onNodeWithText("Continuar").performClick()

        // En Details se ve el texto y botón "Volver"
        composeRule.onNodeWithText("Segunda pantalla", substring = false).assertExists()
        composeRule.onNodeWithText("Volver", substring = false).assertExists()
        composeRule.onNodeWithText("Volver").performClick()

        // De regreso a Home
        composeRule.onNodeWithText("Continuar", substring = false).assertExists()
    }
}