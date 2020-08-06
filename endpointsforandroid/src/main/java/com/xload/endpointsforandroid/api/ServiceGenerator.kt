package com.xload.endpointsforandroid.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
object ServiceGenerator {

    private const val BASE_URL_DEVELOPMENT = "https://xld-development.appspot.com/"
    private const val BASE_URL_PRODUCTION = "https://xld-development.appspot.com/"

    @JvmStatic
    fun getRetrofitInstance(isDevelopment: Boolean = true): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val baseUrl = if (isDevelopment) BASE_URL_DEVELOPMENT else BASE_URL_PRODUCTION
        val retrofitBuilder =  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)

        return retrofitBuilder.build()
    }

    @JvmStatic
    fun getEndpointForPartnerApi(isDevelopment: Boolean): EndpointForPartnerApi {
        return getRetrofitInstance(isDevelopment).create(EndpointForPartnerApi::class.java)
    }



}