package com.hilmysf.fundamental.presentation.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.hilmysf.fundamental.data.local.SettingPreferences
import com.hilmysf.fundamental.helper.DailyReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val pref: SettingPreferences
) : ViewModel() {
    val notificationName = "daily_reminder_work"
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

    fun saveReminderSetting(dailyReminder: Boolean, context: Context) {
        viewModelScope.launch {
            pref.saveReminderSetting(dailyReminder)
        }
        if (dailyReminder) {
            startDailyReminder(context)
        } else {
            cancelDailyReminder(context)
        }
    }

    private fun cancelDailyReminder(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(notificationName)
    }

    private fun startDailyReminder(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("daily_reminder_tag")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            notificationName,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }
}


data class SettingItem(
    val id: String,
    val title: String,
    val description: String,
    val isChecked: Boolean
)