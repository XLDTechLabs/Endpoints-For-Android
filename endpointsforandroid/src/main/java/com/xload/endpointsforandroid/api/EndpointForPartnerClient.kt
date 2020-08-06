package com.xload.endpointsforandroid.api

/**
 * @author John Paul Cas
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class EndpointForPartnerClient {

    companion object {
        private var instance: EndpointForPartnerClient? = null

        @JvmStatic
        fun getInstance(): EndpointForPartnerClient? {
            if (instance == null) {
                instance = EndpointForPartnerClient()
            }

            return instance
        }
    }

    fun start() {

    }

}