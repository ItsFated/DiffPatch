package com.im_hero.diffpatch

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object HttpUtils {
    const val HOST_URL = "http://192.168.18.38:8080"
    const val GET_LATEST_VERSION = "/app/latest-version"
    const val GET_LATEST_PATCH = "/app/get-apk-patch"

    fun getLatestVersion(): JSONObject {
        val json: JSONObject
        val url = URL("$HOST_URL$GET_LATEST_VERSION")
        val urlConn: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConn.doOutput = true
        urlConn.doInput = true
        val reader = BufferedReader(InputStreamReader(urlConn.inputStream))
        urlConn.connect()
        json = JSONObject(reader.readLine())
        Log.i(App.TAG, json.toString())
        reader.close()
        urlConn.disconnect()
        return json
    }

    fun downloadLatestPatch(context: Context, patchFileName: String): Long {
        val request = DownloadManager.Request(Uri.parse("$HOST_URL$GET_LATEST_PATCH?current-version-code=${App.versionCode}"))
//        request.setDestinationUri(Uri.fromFile(File(context.cacheDir, patchFileName)))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, patchFileName)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        request.setTitle(context.getString(R.string.app_name))
        request.setDescription(context.getString(R.string.format_patch_file, patchFileName))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setVisibleInDownloadsUi(true)
        //7.0以上的系统适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresDeviceIdle(false)
            request.setRequiresCharging(false)
        }
        return (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
    }
}