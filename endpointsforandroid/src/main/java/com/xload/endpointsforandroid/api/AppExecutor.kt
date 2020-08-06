package com.xload.endpointsforandroid.api

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class AppExecutors {

    private val _networkIO = Executors.newScheduledThreadPool(3)

    val networkIO: ScheduledExecutorService
        get() = _networkIO

    companion object {
        private var instance: AppExecutors? = null

        fun getInstance(): AppExecutors {
            if (instance == null) {
                instance = AppExecutors()
            }

            return instance as AppExecutors
        }
    }
}