package com.guet.flexbox

import androidx.annotation.MainThread

class EventBus : EventHandler {

    private val channels = HashMap<String, HashMap<Class<*>, Channel<*>>>()

    override fun onEvent(key: String, action: Any) {
        channels[key]?.get(action.javaClass)?.let {
            @Suppress("UNCHECKED_CAST")
            it as Channel<Any>
        }?.handleEvent(action)
    }

    @MainThread
    fun remove(key: String, type: Class<*>) {
        channels[key]?.let {
            it.remove(type)
            if (it.isEmpty()) {
                channels.remove(key)
            }
        }
    }

    @MainThread
    fun channel(key: String, handler: Channel<Any>) {
        channel(key, Any::class.java, handler)
    }

    @MainThread
    fun <T> channel(key: String, type: Class<T>, handler: Channel<T>) {
        channels.getOrPut(key) { HashMap() }[type] = handler
    }

    @FunctionalInterface
    interface Channel<T> {
        fun handleEvent(value: T)
    }
}