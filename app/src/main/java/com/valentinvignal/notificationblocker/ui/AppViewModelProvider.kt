package com.valentinvignal.notificationblocker.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.valentinvignal.notificationblocker.NotificationBlockerApplication
import com.valentinvignal.notificationblocker.ui.group.GroupViewModel
import com.valentinvignal.notificationblocker.ui.home.HomeViewModel
import com.valentinvignal.notificationblocker.ui.home.StartButtonViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val database = database().database;
            HomeViewModel(database.groupDao, database.appIdDao)
        }
        initializer {
            val database = database().database;
            GroupViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                dao = database.groupDao,
                groupToAppDao = database.groupToAppDao,
            )
        }
        initializer {
            val database = database().database;
            StartButtonViewModel(
                groupToAppDao = database.groupToAppDao,
                appIdDao = database.appIdDao,
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