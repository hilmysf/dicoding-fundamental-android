package com.hilmysf.fundamental.presentation.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.data.local.SettingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: SettingPreferences
) : ViewModel() {
    private val settings = listOf(
        SettingItem(
            id = "dark-mode",
            title = "Dark Mode",
            description = "Enable dark mode",
            isChecked = false
        ),
        SettingItem(
            id = "daily-reminder",
            title = "Daily Reminder",
            description = "Enable notification",
            isChecked = false
        )
    )

    val settingItems: StateFlow<List<SettingItem>> = combine(
        pref.getThemeSetting(),
        pref.getReminderSetting()
    ) { isDark, isReminder ->
        Log.d("SettingViewModel", "settingItems: $isDark, $isReminder")
        listOf(
            SettingItem(
                id = "dark-mode",
                title = "Dark Mode",
                description = "Enable dark mode",
                isChecked = isDark
            ),
            SettingItem(
                id = "daily-reminder",
                title = "Daily Reminder",
                description = "Enable notification",
                isChecked = isReminder
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = settings
    )

    fun saveThemeSetting(isDark: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDark)
        }
    }

    fun saveReminderSetting(dailyReminder: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(dailyReminder)
        }
    }
}


data class SettingItem(
    val id: String,
    val title: String,
    val description: String,
    val isChecked: Boolean
)