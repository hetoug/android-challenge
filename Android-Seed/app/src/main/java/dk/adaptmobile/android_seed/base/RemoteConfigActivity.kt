package dk.adaptmobile.android_seed.base

import android.os.Bundle
import com.github.ajalt.timberkt.Timber.d
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dk.adaptmobile.amkotlinutil.extensions.versionNumberToInt
import dk.adaptmobile.android_seed.BuildConfig
import dk.adaptmobile.android_seed.R
import dk.adaptmobile.android_seed.navigation.BaseActivity

open class RemoteConfigActivity : BaseActivity() {

    private var minimumVersionDialogShowing = false
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    companion object {
        private const val RC_MINIMUM_VERSION = "minimum_version_android"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(60L).build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    override fun onResume() {
        super.onResume()
        fetchRemoteConfigs()
    }

    private fun fetchRemoteConfigs() {
        firebaseRemoteConfig.fetch(BuildConfig.REMOTE_CONFIG_CACHE_EXPIRATION_SECONDS).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                d { "Remote config succeeded" }

                // Once the config is successfully fetched it must be activated before newly fetched values are returned.
                firebaseRemoteConfig.activate()

                if (!checkMinimumVersion() && !minimumVersionDialogShowing) {
                    onMinimumVersionChecked() // continue execution if app is up to date)
                }
            } else {
                // fetch remoteconfig failed, dont block activities reliant on the callback
                onMinimumVersionChecked()
            }
        }
    }

    protected open fun onMinimumVersionChecked() {}
    protected open fun showMinimumVersionDialog() {}

    private fun checkMinimumVersion(): Boolean {
        val requiredVersion = firebaseRemoteConfig.getString(RC_MINIMUM_VERSION)
        val appVersion = BuildConfig.VERSION_NAME

        val shouldPromptForUpdate = requiredVersion.versionNumberToInt() > appVersion.versionNumberToInt()

        if (shouldPromptForUpdate && !minimumVersionDialogShowing) {
            minimumVersionDialogShowing = true
            showMinimumVersionDialog()
            return true
        }

        return false
    }
}
