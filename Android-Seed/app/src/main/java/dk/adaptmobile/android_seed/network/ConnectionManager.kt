package dk.adaptmobile.android_seed.network

import android.annotation.SuppressLint
import com.google.gson.GsonBuilder
import dk.adaptmobile.android_seed.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
object ConnectionManager {
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

        val gson = GsonBuilder()
                .create()

        client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create(gson)) // had to switch back to gson due to moshi bug slowing down the first request https://github.com/square/moshi/issues/362
                .build()

        restService = retrofit.create(RestService::class.java)
    }
}