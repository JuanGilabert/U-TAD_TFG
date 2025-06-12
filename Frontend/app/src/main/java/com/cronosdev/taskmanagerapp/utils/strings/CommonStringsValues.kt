package com.cronosdev.taskmanagerapp.utils.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cronosdev.taskmanagerapp.R

object CommonStringsValues {
    //
    @Composable
    fun backButtonValue(): String = stringResource(R.string.backButtonValue)
    //
    @Composable
    fun createButtonValue(): String = stringResource(R.string.createButtonValue)
    //
    @Composable
    fun updateButtonValue(): String = stringResource(R.string.updateButtonValue)
    //
    @Composable
    fun deleteButtonValue(): String = stringResource(R.string.deleteButtonValue)
}