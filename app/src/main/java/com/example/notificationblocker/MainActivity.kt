package com.example.notificationblocker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.notificationblocker.ui.theme.NotificationBlockerTheme
import com.example.notificationblocker.ui.home.StartButton


// https://medium.com/@jpmtech/intro-to-room-using-jetpack-compose-38d078cdb43d
// https://developer.android.com/courses/pathways/android-basics-compose-unit-6-pathway-2

class MainActivity : ComponentActivity() {


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
                        // Your UI content
                    }
                }
            }
        }
    }
}
