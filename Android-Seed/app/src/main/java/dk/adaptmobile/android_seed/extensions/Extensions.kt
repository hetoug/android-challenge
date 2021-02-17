package dk.adaptmobile.android_seed.extensions

import android.webkit.WebView
import android.webkit.WebViewClient
import dk.adaptmobile.android_seed.managers.CacheManager
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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

sealed class WebViewEvent {
    data class ShouldOverrideUrlLoading(val url: String) : WebViewEvent()
    data class OnPageFinished(val url: String) : WebViewEvent()
}

fun WebView.events(): Observable<WebViewEvent> {
    return Observable.create { emitter ->
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                emitter.onNext(WebViewEvent.ShouldOverrideUrlLoading(url))
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                emitter.onNext(WebViewEvent.OnPageFinished(url))
            }
        }
    }
}
