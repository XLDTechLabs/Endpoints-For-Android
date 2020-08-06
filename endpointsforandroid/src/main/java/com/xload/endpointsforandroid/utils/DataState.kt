package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class DataState<T>(
    var message: StateMessage? = null,
    var loading: Boolean = false,
    var data: T? = null
) {

    companion object {
        fun <T> message(message: StateMessage): DataState<T> {
            return DataState(
                message = message,
                loading = false,
                data = null
            )
        }

        fun <T> loading(
            isLoading: Boolean
        ): DataState<T> {
            return DataState(
                message = StateMessage(Response(message = "Loading...", messageType = MessageType.Loading)),
                loading = isLoading,
                data = null
            )
        }

        fun <T> data(
            message: StateMessage? = null,
            data: T? = null
        ): DataState<T> {
            return DataState(
                message = message,
                loading = false,
                data = data
            )
        }

    }

    override fun toString(): String {
        return "message = ${message}, loading = ${loading} data = ${data}"
    }
}