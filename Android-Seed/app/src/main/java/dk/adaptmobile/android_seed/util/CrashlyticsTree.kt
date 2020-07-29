package dk.adaptmobile.android_seed.util

import android.util.Log.ERROR
import android.util.Log.VERBOSE
import android.util.Log.WARN
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dk.adaptmobile.android_seed.extensions.getEnhancedStackTrace
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class CrashlyticsTree() : Timber.Tree(), KoinComponent {

    private val crashlytics: FirebaseCrashlytics by inject()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            in VERBOSE..WARN -> crashlytics.log("Tag: $tag, Message: $message, Throwable: $t")
            ERROR -> crashlytics.recordException(Throwable(message, t).getEnhancedStackTrace())
        }
    }
}
