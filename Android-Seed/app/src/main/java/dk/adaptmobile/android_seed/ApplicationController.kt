package dk.adaptmobile.android_seed

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import dk.adaptmobile.android_seed.util.CrashlyticsTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber

@Suppress("ConstantConditionIf")
class ApplicationController : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Fabric.with(this, Crashlytics())
            Timber.plant(CrashlyticsTree())
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(LinkingDebugTree())
        }
    }
}

class LinkingDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException(
                "Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        val clazz = extractClassName(stackTrace[CALL_STACK_INDEX])
        val lineNumber = stackTrace[CALL_STACK_INDEX].lineNumber
        val logMessage = ".($clazz.kt:$lineNumber) - $message"
        super.log(priority, tag, logMessage, t)
    }

    /**
     * Extract the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     */
    private fun extractClassName(element: StackTraceElement): String {
        var tag = element.className
        tag = tag.split("$")[0]
        return tag.substring(tag.lastIndexOf('.') + 1)
    }

    companion object {
        private const val CALL_STACK_INDEX = 5
    }
}
