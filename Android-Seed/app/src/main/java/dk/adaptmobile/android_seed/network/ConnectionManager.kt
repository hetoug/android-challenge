package dk.adaptmobile.android_seed.network

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.icapps.niddler.core.AndroidNiddler
import com.icapps.niddler.core.Niddler
import com.icapps.niddler.interceptor.okhttp.NiddlerOkHttpInterceptor
import com.icapps.niddler.retrofit.NiddlerRetrofitCallInjector
import com.icapps.niddler.retrofit.NiddlerRetrofitCallInjector.inject
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dk.adaptmobile.android_seed.BuildConfig
import dk.adaptmobile.android_seed.base.ApplicationController
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import org.koin.android.ext.android.inject
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@SuppressLint("StaticFieldLeak")
class ConnectionManager(private val niddler: Niddler) : KoinComponent {
    private var retrofit: Retrofit
    private var client: OkHttpClient
    internal var restService: RestService

    init {
        val interceptor = HttpLoggingInterceptor()

        when (BuildConfig.HTTP_LOGLEVEL) {
            0 -> interceptor.level = HttpLoggingInterceptor.Level.NONE
            1 -> interceptor.level = HttpLoggingInterceptor.Level.BASIC
            2 -> interceptor.level = HttpLoggingInterceptor.Level.BODY
            else -> interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(NiddlerOkHttpInterceptor(niddler, "Default"))
                .build()

        val moshi = Moshi.Builder()
                .add(JavaDateAdapter())
                .add(KotlinJsonAdapterFactory())
                //.add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi)).apply {
                    inject(this, niddler, client)
                }.build()

        restService = retrofit.create(RestService::class.java)
    }
}
