package com.im_hero.diffpatch

import android.app.Application
import android.util.Log
import android.provider.MediaStore
import android.content.ContentResolver
import android.content.Context
import android.net.Uri

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

        fun getRealFilePath(context: Context, uri: Uri): String? {
            val scheme = uri.scheme
            var data: String? = null
            if (scheme == null)
                data = uri.path
            else if (ContentResolver.SCHEME_FILE == scheme) {
                data = uri.path
            } else if (ContentResolver.SCHEME_CONTENT == scheme) {
                val cursor = context.contentResolver.query(uri, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                        if (index > -1) {
                            data = cursor.getString(index)
                        }
                    }
                    cursor.close()
                }
            }
            return data
        }
    }
}