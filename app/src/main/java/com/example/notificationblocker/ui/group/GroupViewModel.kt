package com.example.notificationblocker.ui.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import com.example.notificationblocker.data.GroupToApp
import com.example.notificationblocker.data.GroupToAppDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: GroupDao,
    private val groupToAppDao: GroupToAppDao,
) : ViewModel() {

    private val id: Int = checkNotNull(savedStateHandle[GroupDestination.itemIdArg])

    /**
     * Holds the group ui state. The data is retrieved from [GroupDao] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<Group> =
        dao.get(id)
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = Group(
                    id = id,
                    name = ""
                )
            )

    val appsState: StateFlow<List<String>> = groupToAppDao.getAllAppIds(id).filterNotNull().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = emptyList(),
    )

    fun updateName(newName: String) {
        viewModelScope.launch {
            dao.rename(Group(id = id, name = newName, active = uiState.value.active))
        }
    }

    fun addApp(appId: String) {
        viewModelScope.launch {
            groupToAppDao.upsert(GroupToApp(groupId = id, appId = appId))
        }
    }

    fun deleteApp(appId: String) {
        viewModelScope.launch {
            groupToAppDao.delete(GroupToApp(groupId = id, appId = appId))
        }
    }

    fun delete() {
        viewModelScope.launch {
            dao.delete(Group(id = id))
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}