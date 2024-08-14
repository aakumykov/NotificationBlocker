package com.valentinvignal.notificationblocker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.valentinvignal.notificationblocker.data.App
import com.valentinvignal.notificationblocker.ui.theme.NotificationBlockerTheme


// https://medium.com/@jpmtech/intro-to-room-using-jetpack-compose-38d078cdb43d
// https://developer.android.com/courses/pathways/android-basics-compose-unit-6-pathway-2
// https://github.com/google-developer-training/basic-android-kotlin-compose-training-inventory-app/tree/main

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.init(this)
        setContent {
            NotificationBlockerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NotificationBlockerApp()
                }
            }
        }
    }

    override fun onStart() {
        Log.d("NB", "MainActivity.onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.d("NB", "MainActivity.onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.d("NB", "MainActivity.onResume")
        super.onResume()
    }
}

