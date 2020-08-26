package com.xload.endpointsforandroid.modules.interactor

import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.repository.EndpointPartnerRepository
import com.xload.endpointsforandroid.repository.EndpointPartnerRepositoryImpl
import com.xload.endpointsforandroid.utils.MessageType
import com.xload.endpointsforandroid.utils.OnXLDListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class XLDInteractorImpl : XLDInteractor {

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
        appId: String,
        walletAddress: String,
        otp: Int,
        partnerUserId: String,
        isDevelopment: Boolean,
        listener: OnXLDListener<LinkedWallet>
    ) {
        val linkedWallet: LinkedWallet = LinkedWallet(
            appId = appId,
            walletAddress = walletAddress,
            otp = otp,
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
                                    appId = appId,
                                    walletAddress = walletAddress,
                                    otp = otp,
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

    override fun getWallet(
        key: String,
        secret: String,
        xldUserId: String,
        isDevelopment: Boolean,
        listener: OnXLDListener<WalletStatus>
    ) {
        val repository: EndpointPartnerRepository = EndpointPartnerRepositoryImpl(isDevelopment)
        CoroutineScope(Dispatchers.IO).launch {
            repository.endpointGetWallet(key, secret, xldUserId).onEach { dataState ->
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