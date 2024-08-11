package com.example.notificationblocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupsViewModel(private val groupDao: GroupDao) : ViewModel() {
    val uiState: StateFlow<List<Group>> = groupDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLISECONDS),
        initialValue = emptyList(),
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

    companion object {
        private const val TIMEOUT_MILLISECONDS = 5_000L
    }
}