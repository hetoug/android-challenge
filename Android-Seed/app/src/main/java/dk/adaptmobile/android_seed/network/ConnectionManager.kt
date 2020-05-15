package dk.adaptmobile.android_seed.network

import android.annotation.SuppressLint
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dk.adaptmobile.android_seed.BuildConfig
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@SuppressLint("StaticFieldLeak")
class ConnectionManager() : KoinComponent {
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
                .build()

        val moshi = Moshi.Builder()
                .add(JavaDateAdapter())
                .add(KotlinJsonAdapterFactory())
                //.add(Date::class.java, Rfc3339DateJsonAdapter())
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        restService = retrofit.create(RestService::class.java)
    }
}
