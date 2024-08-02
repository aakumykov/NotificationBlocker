package com.example.notificationblocker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.notificationblocker.data.NotificationBlockerDatabase
import com.example.notificationblocker.ui.group.GroupViewModel
import com.example.notificationblocker.ui.theme.NotificationBlockerTheme
import com.example.notificationblocker.ui.home.StartButton


// https://medium.com/@jpmtech/intro-to-room-using-jetpack-compose-38d078cdb43d
// https://developer.android.com/courses/pathways/android-basics-compose-unit-6-pathway-2

class MainActivity : ComponentActivity() {
private val database by lazy {
    databaseBuilder(
        context= applicationContext,
        klass = NotificationBlockerDatabase::class.java,
        name = "notification_blocker.db",
    ).build()
}

    private val viewModel by viewModels<GroupViewModel> (
        factoryProducer = {
            object: ViewModelProvider.Factory {
                override  fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return GroupViewModel(database.groupDao) as T;
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("NB", "MainActivity.onCreate")
        setContent {
            NotificationBlockerTheme {
                Scaffold(floatingActionButton = { StartButton() }) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        GroupListView(viewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun GroupListView(model: GroupViewModel) {
    val list = model.getAll().collectAsState(initial = emptyList())
    Column{
        Button(
            onClick= {
                model.seedData()
            }
        ) {
            Text("Add")
        }
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(all = 16.dp)
        ) {
            items(list.value) { item ->
                ListItem (
                    headlineContent = {
                        Text(item.name)
                    },
                    supportingContent = {
                        Text(item.id.toString())
                    }

                )
            }
        }
    }
}