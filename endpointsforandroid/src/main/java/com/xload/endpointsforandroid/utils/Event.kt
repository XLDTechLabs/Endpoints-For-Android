package com.xload.endpointsforandroid.utils

/**
 * @author John Paul Cas
 * Created by John Paul Cas on 04/08/2020.
 * Copyright (c) 2020 XLD Tech Labs. All rights reserved.
 */
class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    override fun toString(): String {
        return "Event(content=$content,hasBeenHandled=$hasBeenHandled)"
    }

    companion object{

        // we don't want an event if there's no data
        fun <T> dataEvent(data: T?): Event<T>?{
            data?.let {
                return Event(it)
            }
            return null
        }

        // we don't want an event if there is no message
        fun messageEvent(message: StateMessage?): Event<StateMessage>?{
            message?.let{
                return Event(message)
            }
            return null
        }
    }

}