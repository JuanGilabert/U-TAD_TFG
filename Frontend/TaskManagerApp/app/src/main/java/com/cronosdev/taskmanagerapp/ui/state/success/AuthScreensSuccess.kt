package com.cronosdev.taskmanagerapp.ui.state.success
//
import androidx.compose.runtime.Composable
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.UiState

/**
 *
 */
object AuthScreensSuccess {
    private var _signupScreenName: String = Destinations.SIGNUP_SCREEN_URL
    private var _signinScreenName: String = Destinations.SIGNIN_SCREEN_URL
    /**
     *
     */
    @Composable
    fun ShowSuccessForSignupScreen(uiState: UiState.Success, onScreenSuccessEvent: @Composable (String) -> Unit) {
        if ((uiState).successType == "screenInit") {
            if ((uiState).successScreenCallName == _signupScreenName) {
                if ((uiState).successMethodName == "startSigningUp") {
                    onScreenSuccessEvent((uiState).successMethodName)
                }
            }
        }
        if ((uiState).successType == "screenRunning") {
            if ((uiState).successScreenCallName == _signupScreenName) {
                if ((uiState).successMethodName == "signup") {
                    onScreenSuccessEvent((uiState).successMethodName)
                }
            }
        }
    }
    /**
     *
     */
    @Composable
    fun ShowSuccessForSigninScreen(uiState: UiState.Success, onScreenSuccessEvent: @Composable (String) -> Unit) {
        if ((uiState).successType == "screenInit") {
            if ((uiState).successScreenCallName == _signinScreenName) {
                if ((uiState).successMethodName == "startSigning") {
                    onScreenSuccessEvent((uiState).successMethodName)
                }
            }
        }
        if ((uiState).successType == "screenRunning") {
            if ((uiState).successScreenCallName == _signinScreenName) {
                if ((uiState).successMethodName == "signin") {
                    onScreenSuccessEvent((uiState).successMethodName)
                }
            }
        }
    }
}