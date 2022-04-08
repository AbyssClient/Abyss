package de.vincentschweiger.phantomclient.config.util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class Exclude

class ExcludeStrategy(val internal: Boolean) : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?) = false
    override fun shouldSkipField(field: FieldAttributes) =
            field.getAnnotation(Exclude::class.java) != null
}

/**
 * Decode JSON content
 */
inline fun <reified T> decode(stringJson: String): T = Gson().fromJson(stringJson, object : TypeToken<T>() {}.type)