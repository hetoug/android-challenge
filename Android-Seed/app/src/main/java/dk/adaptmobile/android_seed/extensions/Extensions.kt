package dk.adaptmobile.android_seed.extensions

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import dk.adaptmobile.android_seed.managers.CacheManager
import dk.adaptmobile.android_seed.util.RxJava3Debug
import hu.akarnokd.rxjava3.debug.RxJavaAssemblyException
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


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

inline fun <reified T> Observable<T>.withCache(expirationSeconds: Int, forceExpirationSeconds: Int = 3600): Observable<T> {
    val cachedValue = CacheManager.getCachedValue<T>()
    val cachedTimestamp = CacheManager.getCacheTimestamp<T>()
    val currentTime = System.currentTimeMillis()
    val shouldUseCache = cachedTimestamp > (currentTime - expirationSeconds * 1000)
    val forceExpiration = cachedTimestamp < (currentTime - forceExpirationSeconds * 1000)
    val thisWithSaving = this.doOnNext { CacheManager.saveValue(it as Any) }
    return when {
        cachedValue != null && !shouldUseCache && !forceExpiration -> Observable.merge(Observable.just(cachedValue), thisWithSaving)
        cachedValue != null && shouldUseCache && !forceExpiration -> Observable.fromArray(cachedValue)
        cachedValue == null || forceExpiration -> thisWithSaving
        else -> this
    }
}

/**
 * Obtain a copy of the original Throwable with an extended StackTrace
 *
 * @param original Original Throwable
 * @return new Throwable with enhanced StackTrace if information was found. *Original Throwable* otherwise
 */
@NonNull
fun Throwable.getEnhancedStackTrace(): Throwable {
    val assembledException = RxJavaAssemblyException.find(this)
    if (assembledException != null) {
        val clearStack = RxJava3Debug.parseStackTrace(assembledException, RxJava3Debug.basePackages)
        val enhancedMessage = this.toString()
        val clearException = Throwable(enhancedMessage)
        clearException.stackTrace = clearStack
        return RxJava3Debug.setRootCause(this, clearException)
    }
    return this
}




