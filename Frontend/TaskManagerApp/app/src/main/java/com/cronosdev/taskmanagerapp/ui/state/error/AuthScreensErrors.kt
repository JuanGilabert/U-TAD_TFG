package com.cronosdev.taskmanagerapp.ui.state.error
//
import androidx.compose.runtime.Composable
import com.cronosdev.taskmanagerapp.ui.components.ApiErrorSnackbar
import com.cronosdev.taskmanagerapp.ui.navigation.Destinations
import com.cronosdev.taskmanagerapp.ui.state.UiState

/**
 *
 */
object AuthScreensErrors : ScreenErrors {
    private var _signupScreenName: String = Destinations.SIGNIN_SCREEN_URL
    private var _signinScreenName: String = Destinations.SIGNUP_SCREEN_URL
    /**
     *
     */
    @Composable
    override fun ShowErrorForSigninScreen(uiState: UiState.Error, onScreenErrorEvent: (String) -> Unit) {
        ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
            if ((uiState).errorType == "throwableErrorType") {
                if ((uiState).errorScreenCallName == _signinScreenName) {
                    if ((uiState).errorMethodName == "signin") onScreenErrorEvent((uiState).errorType)
                }
            }
            if ((uiState).errorType == "fetchDataErrorType") {
                if ((uiState).errorScreenCallName == _signinScreenName) {
                    if ((uiState).errorMethodName == "signin") onScreenErrorEvent((uiState).errorType)
                }
            }
        })
    }
    @Composable
    fun ShowErrorForSignupScreen(uiState: UiState.Error, onScreenErrorEvent: (String) -> Unit) {
        ApiErrorSnackbar(message = (uiState).errorMessage, onApiErrorSnackBarButtonRetry = {
            if ((uiState).errorType == "throwableErrorType") {
                if ((uiState).errorScreenCallName == _signinScreenName) {
                    if ((uiState).errorMethodName == "signup") onScreenErrorEvent((uiState).errorType)
                }
            }
            if ((uiState).errorType == "fetchDataErrorType") {
                if ((uiState).errorScreenCallName == _signinScreenName) {
                    if ((uiState).errorMethodName == "signup") onScreenErrorEvent((uiState).errorType)
                }
            }
        })
    }
}