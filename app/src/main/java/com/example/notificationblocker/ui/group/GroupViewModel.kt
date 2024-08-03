package com.example.notificationblocker.ui.group

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GroupViewModel(
    savedStateHandle: SavedStateHandle,
    private val dao: GroupDao,): ViewModel()
{

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
                    id= id,
                    name = ""
                )
            )


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}