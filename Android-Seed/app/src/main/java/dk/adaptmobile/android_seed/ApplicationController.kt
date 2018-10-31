package dk.adaptmobile.android_seed

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

/**
 * Created by elek on 16/11/16.
 */

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return // This process is dedicated to LeakCanary for heap analysis. You should not init your app in this process.
        }
        LeakCanary.install(this)

        Fabric.with(this, Crashlytics())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
