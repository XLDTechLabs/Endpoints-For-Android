package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val messageType: MessageType
)

sealed class MessageType{

    object Success: MessageType()

    object Error: MessageType()

    object Info: MessageType()

    object None: MessageType()

    object Loading: MessageType()

}