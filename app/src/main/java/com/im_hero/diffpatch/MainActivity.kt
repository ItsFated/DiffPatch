package com.im_hero.diffpatch

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.im_hero.diffpatch.java.Permissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {
    val RC_INSTALL_PERMISSION = 1
    lateinit var permissions: Permissions
    lateinit var permissionsHandler: PermissionsHandler
    var latestVersionCode = 0
    var latestVersionName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pHandler = PermissionsHandler()
        permissions = Permissions(this)
                .setDefaultPermissionsDenied(pHandler)
                .setDefaultPermissionsGranted(pHandler)
                .setDefaultPermissionsShowRationale(pHandler)
        permissionsHandler = pHandler
        permissions.request(Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)

        tvCurrentVersionCode.text = getString(R.string.format_version_code, App.versionCode)
        tvCurrentVersionName.text = getString(R.string.format_version_name, App.versionName)
        latestVersionCode = App.versionCode
        latestVersionName = App.versionName

        // 获取最新的版本
        async {
            val json = HttpUtils.getLatestVersion()
            latestVersionCode = json.getInt("versionCode")
            latestVersionName = json.getString("versionName")
            runOnUiThread {
                tvNewVersionCode.text = getString(R.string.format_version_code, latestVersionCode)
                tvNewVersionName.text = getString(R.string.format_version_name, latestVersionName)
            }
        }.start()

        btnUpdate.setOnClickListener {
            if (latestVersionCode > App.versionCode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Permissions.hasInstallPermissionWithO(this@MainActivity)) {
                        downloadPatchFile()
                    } else {
                        Permissions.requestInstallPermissionSettingActivity(this@MainActivity, RC_INSTALL_PERMISSION)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Already Latest Version", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RC_INSTALL_PERMISSION -> downloadPatchFile()
            }
        }
    }

    fun downloadPatchFile() {
        // 下载新版本的补丁文件
        HttpUtils.downloadLatestPatch(this@MainActivity, Integer.toString(App.versionCode) + '-' + latestVersionCode + ".patch")
        Log.i(App.TAG, "Start Download")
    }

    inner class PermissionsHandler: Permissions.PermissionsDenied, Permissions.PermissionsShowRationale, Permissions.PermissionsGranted {
        override fun onPermissionsGranted(permissions: Array<out String>?) {}
        override fun onPermissionsDenied(permissions: Array<out String>?): Boolean = true
        override fun onPermissionsShowRationale(permissions: Array<out String>?): Boolean = true
    }
}
