package com.xload.endpointsforandroid.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object ServiceGenerator {

    private const val BASE_URL = "https://recipesapi.herokuapp.com"

    private val retrofitBuilder =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = retrofitBuilder.build()

    private val _endpointForPartnerApi = retrofit.create(EndpointForPartnerApi::class.java)

    val endpointForPartnerApi: EndpointForPartnerApi
        get() = _endpointForPartnerApi

}