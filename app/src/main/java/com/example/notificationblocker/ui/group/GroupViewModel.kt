package com.example.notificationblocker.ui.group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.data.GroupDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GroupViewModel(private val dao: GroupDao): ViewModel()
{

    fun seedData() {
        viewModelScope.launch {
            (0..10).forEach { _ ->
                val group = Group(
                    name = "Group ${(0..1000).random()}"
                )
                dao.upsert(group)
            }
        }
    }

    fun getAll(): Flow<List<Group>> {
        return dao.getAll();
    }

}