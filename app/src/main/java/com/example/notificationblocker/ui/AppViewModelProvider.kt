package com.example.notificationblocker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notificationblocker.NotificationBlockerApplication
import com.example.notificationblocker.ui.group.GroupViewModel
import com.example.notificationblocker.ui.home.GroupsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            GroupsViewModel(database().database.groupDao)
        }
        initializer {
            GroupViewModel(
                this.createSavedStateHandle(),
                database().database.groupDao,
            )
        }
    }

}


/**
 * Extension function to queries for [Application] object and returns an instance of
 * [NotificationBlockerApplication].
 */
fun CreationExtras.database(): NotificationBlockerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NotificationBlockerApplication)