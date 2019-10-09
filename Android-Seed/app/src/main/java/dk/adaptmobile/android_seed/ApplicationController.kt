package dk.adaptmobile.android_seed

import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import dk.adaptmobile.android_seed.util.CrashlyticsTree
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Created by elek on 16/11/16.
 */

@Suppress("ConstantConditionIf")
class ApplicationController : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Fabric.with(this, Crashlytics())
            Timber.plant(CrashlyticsTree())
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
