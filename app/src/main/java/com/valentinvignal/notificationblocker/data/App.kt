package com.valentinvignal.notificationblocker.data

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log


data class App(
    val id: String,
    val name: String,
    val icon: Drawable,
) {
    companion object {

        private var _allApps: List<App>? = null
        val allApps: List<App>
            get() = _allApps!!

        fun init(context: Context) {
            Log.d("NB", "Get all apps $_allApps")
            val packageManager = context.packageManager;
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            _allApps = packageManager.queryIntentActivities(mainIntent, 0).sortedBy { it ->
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
        }
    }
}
