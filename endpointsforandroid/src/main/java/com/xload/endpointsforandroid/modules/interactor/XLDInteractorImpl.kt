package com.xload.endpointsforandroid.modules.interactor

import android.app.Activity
import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.xload.endpointsforandroid.api.models.EndpointStart
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.modules.models.Generated
import com.xload.endpointsforandroid.modules.state.XLDState
import com.xload.endpointsforandroid.modules.state.XLDStateImpl
import com.xload.endpointsforandroid.repository.EndpointPartnerRepository
import com.xload.endpointsforandroid.repository.EndpointPartnerRepositoryImpl
import com.xload.endpointsforandroid.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class XLDInteractorImpl : XLDInteractor {

    private val xldState: XLDState = XLDStateImpl()

    override fun init(
        activity: Activity,
        isDevelopment: Boolean,
        listener: OnXLDConnectionListener
    ) {
        if (!Permissions.hasReadExternalStoragePermission(activity)) {
            listener.onConnectionError(XLDError.NoReadPermission)
        } else if (!xldState.isXLoadAppInstalled(activity, isDevelopment)) {
            listener.onConnectionError(XLDError.XLoadNotInstall)
        } else {
            val folder = File(Environment.getExternalStorageDirectory(), AppConstant.FOLDER_NAME)

            if (folder.exists()) {
                val file = File(folder, AppConstant.FILE_NAME)

                try {
                    val result = FileInputStream(file).bufferedReader().use { it.readLine() }

                    val gson = Gson()
                    val key = gson.fromJson(result.toString(), Generated::class.java)
                    listener.onConnectionSuccess(xldState.encrypt(key.generated))
                } catch (ignore: FileNotFoundException) {
                    listener.onConnectionError(XLDError.XLoadAppNotLogin)
                } catch (error: IOException) {
                    Log.d("DEBUG", "Error ${error.message}")
                    listener.onConnectionError(XLDError.UnknownError(error.message))
                }
            } else {
                listener.onConnectionError(XLDError.XLoadAppNotLogin)
            }
        }
    } // end of init() function

    override fun start(
        key: String,
        client: String,
        deviceId: String,
        appId: String,
        isDevelopment: Boolean,
        onStartListener: OnXLDStartListener
    ) {
        val repository: EndpointPartnerRepository = EndpointPartnerRepositoryImpl(isDevelopment)

        CoroutineScope(Dispatchers.IO).launch {
            repository.endpointPartnerStart(
                key = key,
                secret = client,
                endpointStart = EndpointStart(
                    deviceId = deviceId,
                    appId = appId
                )
            ).onEach { dataState ->
                withContext(Dispatchers.Main) {
                    dataState.message?.response?.let { response ->
                        when (response.messageType) {
                            is MessageType.Success -> {
                                val xldUserId = dataState.data?.xldUserId
                                if (!xldUserId.isNullOrBlank()) {
                                    onStartListener.loading(false)
                                    onStartListener.success(key = xldUserId)
                                } else {
                                    onStartListener.loading(false)
                                    onStartListener.error("Something went wrong. Please try again.")
                                }
                            }
                            is MessageType.Error -> {
                                onStartListener.loading(false)
                                onStartListener.error(response.message)
                            }
                            is MessageType.Loading -> {
                                onStartListener.loading(dataState.loading)
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    } // end of start() function

    override fun conversion(
        key: String,
        secret: String,
        appId: String,
        isDevelopment: Boolean,
        listener: OnXLDListener<Double>
    ) {
        val repository: EndpointPartnerRepository = EndpointPartnerRepositoryImpl(isDevelopment)
        CoroutineScope(Dispatchers.IO).launch {
            repository.endpointGetConversion(key, secret, appId).onEach { dataState ->
                // handle each response
                withContext(Dispatchers.Main) {
                    dataState.message?.response?.let { response ->
                        when (response.messageType) {
                            is MessageType.Success -> {
                                val conversion = dataState.data?.conversion
                                listener.loading(false)
                                listener.success(conversion)
                            }
                            is MessageType.Error -> {
                                listener.loading(false)
                                listener.error(response.message)
                            }
                            is MessageType.Loading -> {
                                listener.loading(dataState.loading)
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    } // end of conversion

    override fun linkWallet(
        key: String,
        secret: String,
        xldUserId: String,
        xldWalletAddress: String,
        xldOtp: Int,
        partnerUserId: String,
        isDevelopment: Boolean,
        listener: OnXLDListener<LinkedWallet>
    ) {
        val linkedWallet: LinkedWallet = LinkedWallet(
            xldUserId = xldUserId,
            xldWalletAddress = xldWalletAddress,
            xldOtp = xldOtp,
            partnerUserId = partnerUserId
        )

        val repository: EndpointPartnerRepository = EndpointPartnerRepositoryImpl(isDevelopment)
        CoroutineScope(Dispatchers.IO).launch {
            repository.endpointLinkWallet(key, secret, linkedWallet).onEach { dataState ->
                // handle each response
                withContext(Dispatchers.Main) {
                    dataState.message?.response?.let { response ->
                        when (response.messageType) {
                            is MessageType.Success -> {
                                val response = dataState.data
                                val responseLinkedWallet = LinkedWallet(
                                    xldUserId = response?.xldUserId,
                                    xldWalletAddress = xldWalletAddress,
                                    xldOtp = xldOtp,
                                    partnerUserId = response?.partnerUserId,
                                    linked = response!!.linked
                                )
                                listener.loading(false)
                                listener.success(responseLinkedWallet)
                            }
                            is MessageType.Error -> {
                                listener.loading(false)
                                listener.error(response.message)
                            }
                            is MessageType.Loading -> {
                                listener.loading(dataState.loading)
                            }
                        }
                    }
                }
            }.launchIn(this)
        }

    } // end linked wallet

    override fun getWalletStatus(
        key: String,
        secret: String,
        xldUserId: String,
        isDevelopment: Boolean,
        listener: OnXLDListener<WalletStatus>
    ) {
        val repository: EndpointPartnerRepository = EndpointPartnerRepositoryImpl(isDevelopment)
        CoroutineScope(Dispatchers.IO).launch {
            repository.endpointGetWalletStatus(key, secret, xldUserId).onEach { dataState ->
                // handle each response
                withContext(Dispatchers.Main) {
                    dataState.message?.response?.let { response ->
                        when (response.messageType) {
                            is MessageType.Success -> {
                                val response = dataState.data
                                val walletStatus = WalletStatus(
                                    xldUserId = response!!.xldUserId,
                                    partnerUserId = response!!.partnerUserId,
                                    linked = response.linked
                                )
                                listener.loading(false)
                                listener.success(walletStatus)
                            }
                            is MessageType.Error -> {
                                listener.loading(false)
                                listener.error(response.message)
                            }
                            is MessageType.Loading -> {
                                listener.loading(dataState.loading)
                            }
                        }
                    }
                }
            }.launchIn(this)
        }
    } // end getWalletStatus


}