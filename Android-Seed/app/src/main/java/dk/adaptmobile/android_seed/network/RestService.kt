package dk.adaptmobile.android_seed.network

import dk.adaptmobile.android_seed.model.TestRequest
import dk.adaptmobile.android_seed.model.TestResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestService {

    @GET("api/v1/test")
    fun getRequest(): Observable<TestResponse>

    @POST("api/v1/test")
    fun postRequest(@Body request: TestRequest): Observable<TestResponse>
}
