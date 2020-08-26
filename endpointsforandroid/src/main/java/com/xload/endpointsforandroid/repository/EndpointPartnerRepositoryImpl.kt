package com.xload.endpointsforandroid.repository

import com.xload.endpointsforandroid.api.ServiceGenerator
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.response.ConversionResponse
import com.xload.endpointsforandroid.api.response.GetWalletStatusResponse
import com.xload.endpointsforandroid.api.response.LinkWalletResponse
import com.xload.endpointsforandroid.utils.DataState
import com.xload.endpointsforandroid.utils.MessageType
import com.xload.endpointsforandroid.utils.Response
import com.xload.endpointsforandroid.utils.StateMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class EndpointPartnerRepositoryImpl(private val isDevelopment: Boolean = true): EndpointPartnerRepository {

    override fun endpointGetConversion(
        key: String,
        secret: String,
        appId: String
    ): Flow<DataState<ConversionResponse>> = flow {
        var result: ConversionResponse? = null
        var dataState: DataState<ConversionResponse> = DataState.loading(true)
        // show loading as initial state
        emit(dataState)
        try {
            result = ServiceGenerator.getEndpointForPartnerApi(isDevelopment).conversion(
                key = key,
                secret = secret,
                appId = appId
            )

            // success response
            if (result.error == null) {
                dataState =
                    DataState.data(StateMessage(Response(null, MessageType.Success)), result)
            } else if (result.error != null) {
                dataState = DataState.message(StateMessage(Response(
                        message = result.error?.message,
                        messageType = MessageType.Error
                    )))
            } else {
                dataState = DataState.message(StateMessage(Response(
                    message = "Unknown Error, Please try again",
                    messageType = MessageType.Error
                )))
            }
            emit(dataState)
        } catch (error: Exception) {
            // error response
            dataState = DataState.message(StateMessage(Response(error.message, MessageType.Error)))
            emit(dataState)
        }
    }

    override fun endpointLinkWallet(
        key: String,
        secret: String,
        linkWallet: LinkedWallet
    ): Flow<DataState<LinkWalletResponse>> = flow {
        var result: LinkWalletResponse? = null
        var dataState: DataState<LinkWalletResponse> = DataState.loading(true)
        // show loading as initial state
        emit(dataState)
        try {
            result = ServiceGenerator.getEndpointForPartnerApi(isDevelopment).linkWallet(
                key = key,
                secret = secret,
                linkedWallet = linkWallet
            )

            // success response
            if (result.error == null) {
                dataState =
                    DataState.data(StateMessage(Response(null, MessageType.Success)), result)
            } else if (result.error != null) {
                dataState = DataState.message(StateMessage(Response(
                    message = result.error?.message,
                    messageType = MessageType.Error
                )))
            } else {
                dataState = DataState.message(StateMessage(Response(
                    message = "Unknown Error, Please try again",
                    messageType = MessageType.Error
                )))
            }
            emit(dataState)
        } catch (error: Exception) {
            // error response
            dataState = DataState.message(StateMessage(Response(error.message, MessageType.Error)))
            emit(dataState)
        }
    }

    override fun endpointGetWalletStatus(
        key: String,
        secret: String,
        xldUserId: String
    ): Flow<DataState<GetWalletStatusResponse>> = flow {
        var result: GetWalletStatusResponse? = null
        var dataState: DataState<GetWalletStatusResponse> = DataState.loading(true)
        // show loading as initial state
        emit(dataState)
        try {
            result = ServiceGenerator.getEndpointForPartnerApi(isDevelopment).getWalletStatus(
                key = key,
                secret = secret,
                xldUserId = xldUserId
            )

            // success response
            if (result.error == null) {
                dataState =
                    DataState.data(StateMessage(Response(null, MessageType.Success)), result)
            } else if (result.error != null) {
                dataState = DataState.message(StateMessage(Response(
                    message = result.error?.message,
                    messageType = MessageType.Error
                )))
            } else {
                dataState = DataState.message(StateMessage(Response(
                    message = "Unknown Error, Please try again",
                    messageType = MessageType.Error
                )))
            }
            emit(dataState)
        } catch (error: Exception) {
            // error response
            dataState = DataState.message(StateMessage(Response(error.message, MessageType.Error)))
            emit(dataState)
        }
    }


}