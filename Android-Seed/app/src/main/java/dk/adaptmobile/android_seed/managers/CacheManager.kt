package dk.adaptmobile.android_seed.managers

import android.annotation.SuppressLint

@SuppressLint("CheckResult")
object CacheManager {

    val networkCache: MutableMap<Any, Long> = mutableMapOf()

    inline fun <reified T> getCacheTimestamp(): Long {
        val key = networkCache.keys.firstOrNull { it is T }
        return networkCache[key] ?: 0
    }

    inline fun <reified T> getCachedValue(): T? {
        return networkCache.keys.firstOrNull { it is T } as? T
    }

    fun <T : Any> saveValue(value: T) {
        networkCache[value] = System.currentTimeMillis()
    }
}