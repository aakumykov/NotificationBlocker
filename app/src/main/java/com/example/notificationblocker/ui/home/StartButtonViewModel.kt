package com.example.notificationblocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.AppIdDao
import com.example.notificationblocker.data.GroupToAppDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn

class StartButtonViewModel(
    private val groupToAppDao: GroupToAppDao,
    private val appIdDao: AppIdDao,

    ) : ViewModel() {

    val apps: StateFlow<Set<String>> =
        groupToAppDao.getAllActiveApps().combine(
            appIdDao.getAll().map { apps -> apps.map { app -> app.id } },

            ) { a, b -> (a + b).toSet() }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLISECONDS),
            initialValue = emptySet(),
        )

    companion object {
        private const val TIMEOUT_MILLISECONDS = 5_000L
    }

}