package dk.adaptmobile.android_seed.usecases

import com.github.ajalt.timberkt.e
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.util.ErrorConverter
import io.reactivex.rxjava3.core.Single
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.adapter.rxjava3.Result

abstract class BaseUseCase : KoinComponent {
    private val errorConverter: ErrorConverter by inject()

    sealed class UseCaseResult<T> {
        data class Success<T>(val body: T) : UseCaseResult<T>()
        data class Failure<T>(val message: ErrorConverter.ErrorMessage) : UseCaseResult<T>()
    }

    internal fun <T, R> Result<T>.toUseCaseResult(function: (it: T) -> R): UseCaseResult<R> {
        val response = response()
        return when (isError || response == null) {
            false -> {
                val body = response.body()
                when (response.isSuccessful && body != null) {
                    true -> UseCaseResult.Success(function(body))
                    false -> UseCaseResult.Failure(errorConverter.toErrorResponse(response) ?: ErrorConverter.ErrorMessage("Generic error"))
                }
            }
            true -> return UseCaseResult.Failure(ErrorConverter.ErrorMessage("Generic error"))
        }
    }


}