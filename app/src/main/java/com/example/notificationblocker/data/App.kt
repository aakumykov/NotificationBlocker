package com.example.notificationblocker.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable


data class App(
    val id: String,
    val name: String,
    val icon: Drawable,
) {
    companion object {

        private var allApps: List<App>? = null;
        fun getAll(context: Context): List<App> {
            if (allApps != null) return allApps!!
            val packageManager = context.packageManager;
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            allApps = packageManager.queryIntentActivities(mainIntent, 0).sortedBy { it ->
                    it.loadLabel(packageManager).toString().lowercase()
                }.map {
                    val resources =
                        packageManager.getResourcesForApplication(it.activityInfo.applicationInfo);
                    val name = if (it.activityInfo.labelRes != 0) {
                        resources.getString(it.activityInfo.labelRes)
                    } else {
                        it.activityInfo.applicationInfo.loadLabel(packageManager).toString()
                    }
                    App(
                        id = it.activityInfo.packageName,
                        name = name,
                        icon = it.activityInfo.loadIcon(packageManager),
                    )
                }.distinctBy { it.id }
            return allApps!!
        }
    }
}
