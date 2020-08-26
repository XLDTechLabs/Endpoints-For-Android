package com.xload.endpointsforandroid.modules

import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.models.WalletStatus
import com.xload.endpointsforandroid.modules.interactor.XLDInteractor
import com.xload.endpointsforandroid.modules.models.BaseXLD
import com.xload.endpointsforandroid.utils.OnXLDListener

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 30/07/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class XLD(val key: String?, val secret: String?) : BaseXLD() {

    constructor() : this(null, null) {
    }

    companion object {
        private var keyInstance: String? = null
        private var secretInstance: String? = null

        private var isDevelopment: Boolean = true
        private var instance: XLD? = null

        @JvmStatic
        fun getInstance(
            key: String? = null,
            secret: String? = null,
            development: Boolean = true
        ): XLD {
            isDevelopment = development

            if (instance == null || keyInstance == null || secretInstance == null) {
                instance = XLD(key, secret)
            }

            keyInstance = key
            secretInstance = secret
            return instance as XLD
        }

    }

    private fun interactor(): XLDInteractor {
        return getInteractor()
    }

    fun linkWallet(
        appId: String,
        walletAddress: String,
        otp: Int,
        partnerUserId: String,
        listener: OnXLDListener<LinkedWallet>
    ) {
        println("DEBUG: key = ${key}, secret = ${secret}")
        if (key.isNullOrBlank() || secret.isNullOrBlank()) {
            throw InstantiationException("Partner key or secret cannot be empty. Please asked for the partner key or secret")
        }
        interactor().linkWallet(
            key = key,
            secret = secret,
            appId = appId,
            walletAddress = walletAddress,
            otp = otp,
            partnerUserId = partnerUserId,
            isDevelopment = isDevelopment,
            listener = listener
        )
    }

    fun getWallet(xldUserId: String, listener: OnXLDListener<WalletStatus>) {
        if (key.isNullOrBlank() || secret.isNullOrBlank()) {
            throw InstantiationException("Partner key or secret cannot be empty. Please asked for the partner key or secret")
        }
        interactor().getWallet(key, secret, xldUserId, isDevelopment, listener)
    }

    fun getConversion(appId: String, listener: OnXLDListener<Double>) {
        if (key.isNullOrBlank() || secret.isNullOrBlank()) {
            throw InstantiationException("Partner key or secret cannot be empty. Please asked for the partner key or secret")
        }
        interactor().conversion(key, secret, appId, isDevelopment, listener)
    }

}