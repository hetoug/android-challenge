package dk.adaptmobile.android_seed

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.squareup.leakcanary.LeakCanary

import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by elek on 16/11/16.
 */

class ApplicationController : Application() {
    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        if (!BuildConfig.DEBUG) {

        }

        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.sanfran_light))
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
