package com.xload.endpointsample

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.xload.endpointsforandroid.modules.XLD
import com.xload.endpointsforandroid.utils.OnXLDConnectionListener
import com.xload.endpointsforandroid.utils.XLDError
import com.xload.endpointsforandroid.utils.XLDUtils
import kotlinx.android.synthetic.main.activity_login.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class LoginActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val REQUEST_STORAGE_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onResume() {
        super.onResume()
        init()
        buttonListener()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasStoragePermission()) {
               initializeXLD()
            } else {
                requestStorage()
                errorStoragePermission()
            }
        } else {
            initializeXLD()
        }
    }

    private fun requestStorage() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.required_storage_permission),
            REQUEST_STORAGE_PERMISSION,
            Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun buttonListener() {
        btnAcceptStoragePermission.setOnClickListener {
            requestStorage()
        }

        btnDownloadXLoadApp.setOnClickListener {
            XLDUtils.installXLoadApp(this@LoginActivity)
        }

        btnSignInXLoadApp.setOnClickListener {
            XLDUtils.redirectToXLoadApp(this@LoginActivity)
        }
    }

    private fun initializeXLD() {
        XLD.init(this, true, object: OnXLDConnectionListener {
            override fun onConnectionError(error: XLDError) {
                handleXLDError(error)
            }

            override fun onConnectionSuccess(key: String) {
                println("DEBUG: key = ${key}")
            }

        })
    }

    private fun handleXLDError(error: XLDError) {
        when (error) {
            is XLDError.XLoadAppNotLogin -> {
                errorSignInXLoadApp()
            }
            is XLDError.XLoadNotInstall -> {
                errorDownloadXLoadApp()
            }
            is XLDError.NoReadPermission -> {
                errorStoragePermission()
            }
            is XLDError.UnknownError -> {
                Log.d("DEBUG", "error ${error.error}")
            }
        }
    }

    private fun requestPermissionDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        requestPermissionDenied(requestCode, perms)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun hasStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return EasyPermissions.hasPermissions(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        return true
    }

    private fun errorStoragePermission() {
        btnAcceptStoragePermission.visibility = View.VISIBLE
        btnDownloadXLoadApp.visibility = View.GONE
        btnSignInXLoadApp.visibility = View.GONE
    }

    private fun errorDownloadXLoadApp() {
        btnAcceptStoragePermission.visibility = View.GONE
        btnDownloadXLoadApp.visibility = View.VISIBLE
        btnSignInXLoadApp.visibility = View.GONE
    }

    private fun errorSignInXLoadApp() {
        btnAcceptStoragePermission.visibility = View.GONE
        btnDownloadXLoadApp.visibility = View.GONE
        btnSignInXLoadApp.visibility = View.VISIBLE
    }
}
