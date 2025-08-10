package com.imkalpesh.expense.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings_prefs")

class ThemePreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode_enabled")
    }

    val isDarkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[DARK_MODE_KEY] = enabled }
    }
}