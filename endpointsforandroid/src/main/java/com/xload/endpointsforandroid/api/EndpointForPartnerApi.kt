package com.xload.endpointsforandroid.api

import com.xload.endpointsforandroid.api.models.EndpointStart
import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.response.ConversionResponse
import com.xload.endpointsforandroid.api.response.GetWalletStatusResponse
import com.xload.endpointsforandroid.api.response.LinkWalletResponse
import com.xload.endpointsforandroid.api.response.StartEndpointResponse
import retrofit2.http.*

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface EndpointForPartnerApi {

    @POST("endpoints/start")
    suspend fun startEndpoint(
        @Header("key") key: String,
        @Header("secret") secret: String,
        @Body endpointStart: EndpointStart
    ): StartEndpointResponse

    @GET("endpoints/conversion")
    suspend fun conversion(
        @Header("key") key: String,
        @Header("secret") secret: String,
        @Query("app_id") appId: String
    ): ConversionResponse

    @POST("endpoints/wallet/link")
    suspend fun linkWallet(
        @Header("key") key: String,
        @Header("secret") secret: String,
        @Body linkedWallet: LinkedWallet
    ): LinkWalletResponse

    @GET("endpoints/wallet/status")
    suspend fun getWalletStatus(
        @Header("key") key: String,
        @Header("secret") secret: String,
        @Query("xld_user_id") xldUserId: String
    ): GetWalletStatusResponse


}