package dk.adaptmobile.android_seed.managers

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.hardware.biometrics.BiometricManager as AndroidBiometricManager
import android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS
import android.os.Build
import androidx.annotation.StringRes
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.BuildCompat
import androidx.fragment.app.FragmentActivity
import com.github.ajalt.timberkt.Timber.e
import dk.adaptmobile.android_seed.R
import io.reactivex.Single
import java.util.concurrent.Executors

/**
 * Biometric authentication manager
 * This will be responsible for handling biometric authentication
 */

object BiometricManager {

    fun hasBiometrics(context: Context?): Single<Boolean> {
        return BiometricChecker.getInstance(context).hasBiometrics
    }

    /**
     * Authenticate the biometric login
     * Will return an enum BiometricStatus
     */
    fun authenticate(activity: Activity?, promptType: AuthenticationPrompt = AuthenticationPrompt.Default): Single<BiometricStatus> {
        activity ?: return Single.just(BiometricStatus.ErrorActivityNull)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(activity.getString(promptType.title))
                .setDescription(activity.getString(promptType.description))
                .setNegativeButtonText(activity.getString(promptType.negativeButtonText))
                .build()

        return createObservable(activity, promptInfo)
    }

    sealed class AuthenticationPrompt(@StringRes val title: Int, @StringRes val description: Int, @StringRes val negativeButtonText: Int) {
        object Default : AuthenticationPrompt(R.string.default_biometric_title, R.string.default_biometric_description, R.string.negativeButtonText)
    }

    /**
     * The cases we realistically will handle are defined here
     */
    sealed class BiometricStatus {
        object ErrorRateLimit : BiometricStatus()
        object ErrorTimeout : BiometricStatus()
        object ErrorNegativeButton : BiometricStatus()
        object ErrorBanned : BiometricStatus()
        object ErrorCancelled : BiometricStatus()
        object ErrorGeneral : BiometricStatus()
        object ErrorActivityNull : BiometricStatus()
        object Success : BiometricStatus() }

    private fun createObservable(activity: Activity?, promptInfo: BiometricPrompt.PromptInfo): Single<BiometricStatus> {
        return Single.create {
            val executor = Executors.newSingleThreadExecutor()
            val fragmentActivity = activity as? FragmentActivity ?: return@create
            val prompt = BiometricPrompt(fragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        BiometricConstants.ERROR_NEGATIVE_BUTTON -> it.onSuccess(BiometricStatus.ErrorNegativeButton)
                        BiometricConstants.ERROR_LOCKOUT -> it.onSuccess(BiometricStatus.ErrorRateLimit)
                        BiometricConstants.ERROR_CANCELED -> it.onSuccess(BiometricStatus.ErrorCancelled)
                        BiometricConstants.ERROR_HW_UNAVAILABLE -> it.onSuccess(BiometricStatus.ErrorGeneral)
                        BiometricConstants.ERROR_NO_SPACE -> it.onSuccess(BiometricStatus.ErrorGeneral)
                        BiometricConstants.ERROR_VENDOR -> it.onSuccess(BiometricStatus.ErrorGeneral)
                        BiometricConstants.ERROR_LOCKOUT_PERMANENT -> it.onSuccess(BiometricStatus.ErrorBanned)
                        BiometricConstants.ERROR_HW_NOT_PRESENT -> it.onSuccess(BiometricStatus.ErrorGeneral)
                        BiometricConstants.ERROR_TIMEOUT -> it.onSuccess(BiometricStatus.ErrorTimeout)
                        else -> e { "There seems to be an unhandled case in the biometric prompt [$errorCode]: $errString" }
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    it.onSuccess(BiometricStatus.Success)
                }
            })

            prompt.authenticate(promptInfo)
        }
    }

    private sealed class BiometricChecker {

        abstract val hasBiometrics: Single<Boolean>

        @TargetApi(Build.VERSION_CODES.Q)
        private class QBiometricChecker(private val biometricManager: AndroidBiometricManager?) : BiometricChecker() {

            override val hasBiometrics: Single<Boolean>
                get() = Single.just(biometricManager?.canAuthenticate() == BIOMETRIC_SUCCESS)

            companion object {
                fun getInstance(context: Context): QBiometricChecker = QBiometricChecker(context.getSystemService(AndroidBiometricManager::class.java))
            }
        }

        @Suppress("DEPRECATION")
        private class LegacyBiometricChecker(private val fingerprintManager: FingerprintManagerCompat) : BiometricChecker() {

            override val hasBiometrics: Single<Boolean>
                @SuppressLint("MissingPermission")
                get() = Single.just(fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints())

            companion object {
                fun getInstance(context: Context): LegacyBiometricChecker = LegacyBiometricChecker(FingerprintManagerCompat.from(context))
            }
        }

        private class DefaultBiometricChecker : BiometricChecker() {
            override val hasBiometrics: Single<Boolean> = Single.just(false)
        }

        companion object {

            @SuppressLint("ObsoleteSdkInt")
            fun getInstance(context: Context?): BiometricChecker {
                context ?: return DefaultBiometricChecker()
                return when {
                    BuildCompat.isAtLeastQ() -> QBiometricChecker.getInstance(context)
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> LegacyBiometricChecker.getInstance(context)
                    else -> DefaultBiometricChecker()
                }
            }
        }
    }
}
