package com.anje.kelvin.aconting.presentation.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.anje.kelvin.aconting.ui.theme.AccountingTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllElements() {
        composeTestRule.setContent {
            AccountingTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // Check if all UI elements are displayed
        composeTestRule.onNodeWithText("Pakitini").assertIsDisplayed()
        composeTestRule.onNodeWithText("Telefone").assertIsDisplayed()
        composeTestRule.onNodeWithText("PIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Entrar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Não tem conta? Registre-se").assertIsDisplayed()
    }

    @Test
    fun loginScreen_inputFields_acceptText() {
        composeTestRule.setContent {
            AccountingTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = {}
                )
            }
        }

        // Test phone number input
        composeTestRule.onNodeWithText("Telefone")
            .performTextInput("123456789")

        // Test PIN input
        composeTestRule.onNodeWithText("PIN")
            .performTextInput("1234")

        // Verify text was entered (for phone field - PIN field would be masked)
        composeTestRule.onNodeWithText("Telefone")
            .assert(hasText("123456789"))
    }

    @Test
    fun loginScreen_clickRegisterButton_triggersNavigation() {
        var registerClicked = false
        
        composeTestRule.setContent {
            AccountingTheme {
                LoginScreen(
                    onLoginSuccess = {},
                    onNavigateToRegister = { registerClicked = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Não tem conta? Registre-se")
            .performClick()

        assert(registerClicked)
    }
}