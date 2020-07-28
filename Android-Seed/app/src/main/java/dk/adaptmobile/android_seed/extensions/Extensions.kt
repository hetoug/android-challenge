package dk.adaptmobile.android_seed.extensions

import dk.adaptmobile.android_seed.managers.CacheManager
import io.reactivex.Flowable
import io.reactivex.Single


inline fun <reified T> Single<T>.withCache(expirationSeconds: Int, forceExpirationSeconds: Int = 3600): Flowable<T> {
    val cachedValue = CacheManager.getCachedValue<T>()
    val cachedTimestamp = CacheManager.getCacheTimestamp<T>()
    val currentTime = System.currentTimeMillis()
    val shouldUseCache = cachedTimestamp > (currentTime - expirationSeconds * 1000)
    val forceExpiration = cachedTimestamp < (currentTime - forceExpirationSeconds * 1000)
    val thisWithSaving = this.doOnSuccess { CacheManager.saveValue(it as Any) }
    return when {
        cachedValue != null && !shouldUseCache && !forceExpiration -> Single.merge(Single.just(cachedValue), thisWithSaving)
        cachedValue != null && shouldUseCache && !forceExpiration -> Flowable.fromArray(cachedValue)
        cachedValue == null || forceExpiration -> thisWithSaving.toFlowable()
        else -> this.toFlowable()
    }
}
