package dk.adaptmobile.android_seed.util

import com.github.ajalt.timberkt.e
import retrofit2.Response
import retrofit2.Retrofit

class ErrorConverter(val retrofit: Retrofit) {
    abstract class ErrorWrapper(var statusCode: Int = 0)
    data class ErrorMessage(val message: String?) : ErrorWrapper()

    inline fun <T, reified T2 : ErrorWrapper> toErrorResponse(response: Response<T>?): T2? {
        var error: T2? = null
        if (response == null) {
            response?.printStackTrace()
        } else {
            val body = response.errorBody()
            val converter = retrofit.responseBodyConverter<T2>(T2::class.java, arrayOfNulls<Annotation>(0))
            try {
                error = converter.convert(body)?.apply {
                    this.statusCode = response.code()
                }
            } catch (exception: Exception) {
                e { "Convert to ErrorResponse error: $exception" }
                error = null
            }
        }
        return error
    }
}
