package dk.adaptmobile.android_seed.usecases

import dk.adaptmobile.android_seed.model.TestRequest
import dk.adaptmobile.android_seed.network.ConnectionManager
import io.reactivex.rxjava3.core.Single

class FetchJsonUseCase(
        private val connectionManager: ConnectionManager
) : BaseUseCase() {
    operator fun invoke(): Single<UseCaseResult<String>> {
        return connectionManager.restService
                .postRequest(TestRequest("Test"))
                .map { it.toUseCaseResult { it.json.test } }
    }
}