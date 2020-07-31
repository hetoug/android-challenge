package dk.adaptmobile.android_seed.network

import dk.adaptmobile.android_seed.model.TestRequest
import dk.adaptmobile.android_seed.model.TestResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestService {

    @POST("https://httpbin.org/anything")
    fun postRequest(@Body request: TestRequest): Single<Result<TestResponse>>
}
