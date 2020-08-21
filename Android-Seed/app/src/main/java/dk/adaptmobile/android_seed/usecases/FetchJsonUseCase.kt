package dk.adaptmobile.android_seed.usecases

import dk.adaptmobile.android_seed.network.RestService
import io.reactivex.rxjava3.core.Single

class FetchJsonUseCase(
    private val restService: RestService
) : BaseUseCase() {
    operator fun invoke(): Single<UseCaseResult<String>> {
        return restService
                .postRequest()
                .map { it.toUseCaseResult { it.json.test } }
    }
}
