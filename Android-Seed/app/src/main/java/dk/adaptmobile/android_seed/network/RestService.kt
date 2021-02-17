package dk.adaptmobile.android_seed.network

import dk.adaptmobile.android_seed.model.NewsApiResponse
import dk.adaptmobile.android_seed.model.TestResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.adapter.rxjava3.Result
import retrofit2.http.GET

interface RestService {

    //@GET("https://httpbin.org/json")
    @GET("https://newsapi.org/v2/everything?q=tesla&from=2021-01-27&sortBy=publishedAt&apiKey=147206070e254dd6b7408ffa90df1cd8")
    fun postRequest(): Single<Result<NewsApiResponse>>
}
