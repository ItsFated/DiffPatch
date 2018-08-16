package com.im_hero.diffpatch

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.im_hero.diffpatch.java.Permissions

class MainActivity : AppCompatActivity() {
    lateinit var permissions: Permissions
    lateinit var permissionsHandler: PermissionsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionsHandler = PermissionsHandler()
        permissions = Permissions(this)
                .setDefaultPermissionsDenied(permissionsHandler)
                .setDefaultPermissionsGranted(permissionsHandler)
                .setDefaultPermissionsShowRationale(permissionsHandler)
        permissions.request(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    inner class PermissionsHandler: Permissions.PermissionsDenied, Permissions.PermissionsShowRationale, Permissions.PermissionsGranted{
        override fun onPermissionsGranted(permissions: Array<out String>?) {}
        override fun onPermissionsDenied(permissions: Array<out String>?): Boolean = true
        override fun onPermissionsShowRationale(permissions: Array<out String>?): Boolean = true
    }
}
