package com.example.notificationblocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.AppId
import com.example.notificationblocker.data.AppIdDao
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val groupDao: GroupDao,

    private val appIdDao: AppIdDao,
) : ViewModel() {
    val groups: StateFlow<List<Group>> = groupDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLISECONDS),
        initialValue = emptyList(),
    )

    val apps: StateFlow<Set<String>> =
        appIdDao.getAll().map { apps -> apps.map { app -> app.id }.toSet() }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLISECONDS),
            initialValue = emptySet(),
        )

    suspend fun addGroup(): Int {
        return viewModelScope.async {
            groupDao.insert(
                Group(
                    id = 0,
                    name = "",
                    active = false,
                )
            )
        }.await().toInt()
    }

    fun toggleGroup(group: Group, value: Boolean) {
        if (value == group.active) return
        viewModelScope.launch {
            groupDao.update(
                Group(
                    id = group.id,
                    name = group.name,
                    active = value,
                )
            )
        }
    }

    fun toggleApp(appId: String, value: Boolean) {
        viewModelScope.launch {
            if (value) {
                appIdDao.insert(AppId(appId))
            } else {
                appIdDao.delete(AppId(appId))
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLISECONDS = 5_000L
    }
}