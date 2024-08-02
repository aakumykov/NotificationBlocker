package com.example.notificationblocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GroupsViewModel(groupDao: GroupDao): ViewModel() {
    val uiState: StateFlow<List<Group>> = groupDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLISECONDS),
        initialValue = emptyList(),
    )

    companion object {
        private const val TIMEOUT_MILLISECONDS = 5_000L
    }
}