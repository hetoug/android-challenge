package dk.adaptmobile.android_seed.network

import dk.adaptmobile.android_seed.model.NewsApiArticle
import dk.adaptmobile.android_seed.model.NewsApiResponse
import dk.adaptmobile.android_seed.network.RestService
import dk.adaptmobile.android_seed.usecases.BaseUseCase
import io.reactivex.rxjava3.core.Single

class FetchNewsUseCase(
        private val restService: RestService
) : BaseUseCase() {
    operator fun invoke(): Single<BaseUseCase.UseCaseResult<List<NewsApiArticle>>> {
        return restService
                .postRequest()
                .map { it.toUseCaseResult { it.articles } }
    }
}