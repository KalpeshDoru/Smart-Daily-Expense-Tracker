package com.imkalpesh.expense.presentation.screen.settings

import androidx.lifecycle.viewModelScope
import com.imkalpesh.expense.base.BaseViewModel
import com.imkalpesh.expense.base.UiEvent
import com.imkalpesh.expense.pref.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferences: ThemePreferences // DataStore helper
) : BaseViewModel() {

    val isDarkMode = themePreferences.isDarkModeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    override fun onEvent(event: UiEvent) {
        when (event) {
            is SettingsUiEvent.OnDarkModeToggled -> {
                viewModelScope.launch {
                    themePreferences.setDarkMode(event.enabled)
                }
            }
        }
    }
}

sealed class SettingsUiEvent : UiEvent() {
    data class OnDarkModeToggled(val enabled: Boolean) : SettingsUiEvent()
}


