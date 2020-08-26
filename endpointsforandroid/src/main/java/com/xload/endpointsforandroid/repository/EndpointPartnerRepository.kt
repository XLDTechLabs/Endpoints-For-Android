package com.xload.endpointsforandroid.repository

import com.xload.endpointsforandroid.api.models.LinkedWallet
import com.xload.endpointsforandroid.api.response.ConversionResponse
import com.xload.endpointsforandroid.api.response.GetWalletStatusResponse
import com.xload.endpointsforandroid.api.response.LinkWalletResponse
import com.xload.endpointsforandroid.utils.DataState
import kotlinx.coroutines.flow.Flow

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
interface EndpointPartnerRepository {

    fun endpointGetConversion(
        key: String,
        secret: String,
        appId: String
    ): Flow<DataState<ConversionResponse>>

    fun endpointLinkWallet(
        key: String,
        secret: String,
        linkWallet: LinkedWallet
    ): Flow<DataState<LinkWalletResponse>>

    fun endpointGetWalletStatus(
        key: String,
        secret: String,
        xldUserId: String
    ): Flow<DataState<GetWalletStatusResponse>>


}