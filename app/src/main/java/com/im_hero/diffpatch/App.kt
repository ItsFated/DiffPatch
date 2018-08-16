package com.im_hero.diffpatch

import android.app.Application
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val info = packageManager.getPackageInfo(packageName, 0)
        versionCode = info.versionCode
        versionName = info.versionName
        Log.i(TAG, "${info.packageName}, $versionName, $versionCode")
        Log.i(TAG, "ApkLocation: $applicationInfo.sourceDir")
    }

    companion object {
        const val TAG: String = "DiffPatch"

        var versionCode: Int = 0
            private set
        var versionName: String = ""
            private set
    }

}