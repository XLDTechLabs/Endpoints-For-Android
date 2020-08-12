package com.xload.endpointsample.java;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xload.endpointsample.Constants;
import com.xload.endpointsample.R;
import com.xload.endpointsforandroid.modules.XLD;
import com.xload.endpointsforandroid.utils.OnXLDConnectionListener;
import com.xload.endpointsforandroid.utils.XLDError;
import com.xload.endpointsforandroid.utils.XLDUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private Button btnAcceptStoragePermission, btnDownloadXLoadApp, btnSignInXLoadApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAcceptStoragePermission = findViewById(R.id.btnAcceptStoragePermission);
        btnDownloadXLoadApp = findViewById(R.id.btnDownloadXLoadApp);
        btnSignInXLoadApp = findViewById(R.id.btnSignInXLoadApp);

        buttonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void buttonListener() {
        btnAcceptStoragePermission.setOnClickListener(v -> {
            requestStorage();
        });

        btnDownloadXLoadApp.setOnClickListener(v -> {
            XLDUtils.installXLoadApp(this);
        });
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasStoragePermission()) {
                initializeXLD();
            } else {
                requestStorage();
                errorStoragePermission();
            }
        } else {
            initializeXLD();
        }
    }

    private void initializeXLD() {
        XLD.getInstance().init(this, new OnXLDConnectionListener() {
            @Override
            public void onConnectionError(@NotNull XLDError error) {
                handleXLDError(error);
            }

            @Override
            public void onConnectionSuccess(@NotNull String key) {
                startHomeActivity(key);
            }
        });
    }

    private void startHomeActivity(String key) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.UNIQUE_ID, key);
        startActivity(intent);
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return EasyPermissions.hasPermissions(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            );
        }

        return true;
    }

    /**
     * Handle sdk error
     */
    private void handleXLDError(XLDError error) {
        Log.d("DEBUG", "error ${error}");
        if (error instanceof XLDError.XLoadAppNotLogin) {
            errorSignInXLoadApp();
        } else if (error instanceof XLDError.XLoadNotInstall) {
            errorDownloadXLoadApp();
        } else if (error instanceof XLDError.NoReadPermission) {
            errorStoragePermission();
        } else if (error instanceof XLDError.UnknownError) {
            Log.d("DEBUG", "error " + ((XLDError.UnknownError) error).getError());
        }
    }

    private void requestStorage() {
        EasyPermissions.requestPermissions(
                this,
                getString(R.string.required_storage_permission),
                REQUEST_STORAGE_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void errorStoragePermission() {
        btnAcceptStoragePermission.setVisibility(View.VISIBLE);
        btnDownloadXLoadApp.setVisibility(View.GONE);
        btnSignInXLoadApp.setVisibility(View.GONE);
    }

    private void errorDownloadXLoadApp() {
        btnAcceptStoragePermission.setVisibility(View.GONE);
        btnDownloadXLoadApp.setVisibility(View.VISIBLE);
        btnSignInXLoadApp.setVisibility(View.GONE);
    }

    private void errorSignInXLoadApp() {
        btnAcceptStoragePermission.setVisibility(View.GONE);
        btnDownloadXLoadApp.setVisibility(View.GONE);
        btnSignInXLoadApp.setVisibility(View.VISIBLE);
    }

    private void requestPermissionDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        requestPermissionDenied(requestCode, perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
