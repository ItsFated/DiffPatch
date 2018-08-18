package com.im_hero.diffpatch

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import java.io.File
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider




class UpdateApkBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(App.TAG, "onReceive: ${intent.action}")
        when (intent.action) {
            DownloadManager.ACTION_DOWNLOAD_COMPLETE -> {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                installApk(context, id)
            }
            DownloadManager.ACTION_VIEW_DOWNLOADS,
            DownloadManager.ACTION_NOTIFICATION_CLICKED -> {
                val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(viewDownloadIntent)
            }
        }
    }

    private fun installApk(context: Context, downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = downloadManager.getUriForDownloadedFile(downloadId)
        val file = App.getRealFilePath(context, uri)
        if (uri != null && file != null) {
            val newApk = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}${File.separator}DiffPatch-2.apk"
            Log.i(App.TAG, "Start Activity: $uri")
            Log.i(App.TAG, newApk)
            BsDiff.bspatch(context.applicationInfo.sourceDir, newApk, file)
            //以下两行代码可以让下载的apk文件被直接安装而不用使用Fileprovider,系统7.0或者以上才启动。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localBuilder = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(localBuilder.build())
            }
            val install = Intent(Intent.ACTION_VIEW)
            install.data = Uri.fromFile(File(newApk))
            install.setDataAndType(install.data, "application/vnd.android.package-archive")
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(install)
            // Android 7.0 需要用于自动 安装APK的代码
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                val apkUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(newApk))//在AndroidManifest中的android:authorities值
//                val install = Intent(Intent.ACTION_VIEW)
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                install.setDataAndType(apkUri, "application/vnd.android.package-archive")
//                context.startActivity(install)
//            } else {
//                val install = Intent(Intent.ACTION_VIEW)
//                install.data = Uri.fromFile(File(newApk))
//                install.setDataAndType(install.data, "application/vnd.android.package-archive")
//                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                context.startActivity(install)
//            }
        } else {
            Log.i(App.TAG, "Start Activity: failure")
        }
    }
}