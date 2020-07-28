package dk.adaptmobile.android_seed.managers

import android.annotation.SuppressLint
import android.os.Bundle
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.firebase.analytics.FirebaseAnalytics
import dk.adaptmobile.android_seed.base.ApplicationController
import dk.adaptmobile.android_seed.base.Dependencies
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import org.koin.core.KoinComponent

@SuppressLint("CheckResult")
class TrackingManager(private val applicationController: ApplicationController, private val firebaseAnalytics: FirebaseAnalytics) : KoinComponent {

    fun subscribeTracking() {
        Dependencies.screenTracking
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { trackScreen(it) },
                        onError = { e(it) { "Error tracking screen" } }
                )

        Dependencies.eventTracking
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = { trackEvent(it) },
                        onError = { e(it) { "Error tracking event" } }
                )
    }

    private fun trackScreen(screen: TrackingScreen) {
        val name = screen.screenName.trim().replace(" ", "_")
        d { "Tracking screen: $name" }
        trackFirebaseScreen(name)
    }

    private fun trackFirebaseScreen(screenName: String) {
        val activity = applicationController.currentActivity ?: return
        firebaseAnalytics.setCurrentScreen(activity, screenName, screenName)
    }

    private fun trackEvent(event: TrackingEvent) {
        val category = event.category.trim().replace(" ", "_")
        val action = event.action.trim().replace(" ", "_")
        val label = event.label.trim().replace(" ", "_")

        trackFirebaseEvent(category, action, label)
    }

    private fun trackFirebaseEvent(categoryName: String, action: String, label: String) {
        val params = Bundle()
        params.putString("action", action)
        params.putString("label", label)
        d { "Tracking event: category: $categoryName action: $action label: $label" }

        firebaseAnalytics.logEvent(categoryName, params)
    }
}

sealed class TrackingScreen(val screenName: String) {
    //object OnboardingIntroduction : TrackingScreen("Onboarding_introduction")
}

sealed class TrackingEvent(val category: String, val action: String, val label: String) {
    //object AdvancedSearchShowPropertiesClicked : TrackingEvent("Avanceret_soegning_Tryk_Vis_boliger", "Tryk|Vis_boliger", "")

}

fun TrackingScreen.track() {
    Dependencies.screenTracking.onNext(this)
}

fun TrackingEvent.track() {
    Dependencies.eventTracking.onNext(this)
}
