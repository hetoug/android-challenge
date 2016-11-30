package dk.adaptmobile.android_seed;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by elek on 16/11/16.
 */

public class ApplicationController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {

        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.sanfran_light))
                .setFontAttrId(R.attr.fontPath)
                .build());


    }
}
