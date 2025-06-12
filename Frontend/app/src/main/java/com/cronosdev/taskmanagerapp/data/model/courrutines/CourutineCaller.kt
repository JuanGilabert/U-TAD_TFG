package com.cronosdev.taskmanagerapp.data.model.courrutines

import kotlin.coroutines.CoroutineContext

data class CoroutineCaller(
    val screenCallName: String,
    val methodCallName: String
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<CoroutineCaller>
    override val key: CoroutineContext.Key<*> = Key
}