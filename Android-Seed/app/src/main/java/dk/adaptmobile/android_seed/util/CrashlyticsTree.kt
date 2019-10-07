package dk.adaptmobile.android_seed.util

import android.util.Log.*
import com.crashlytics.android.Crashlytics
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            in VERBOSE..WARN -> Crashlytics.log("Tag: $tag, Message: $message, Throwable: $t")
            ERROR -> Crashlytics.logException(t)

        }
    }
}
