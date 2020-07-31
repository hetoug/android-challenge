package dk.adaptmobile.android_seed.network

import dk.adaptmobile.android_seed.model.TestResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.GET

interface RestService {

    @GET("https://httpbin.org/json")
    fun postRequest(): Single<Result<TestResponse>>
}
