package dk.adaptmobile.android_seed.managers

import dk.adaptmobile.amkotlinutil.extensions.toDate
import dk.adaptmobile.android_seed.BuildConfig
import khronos.days
import khronos.minute
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

object ReviewManager : KoinComponent {

    private val prefsManager: PrefsManager by inject()

    fun favoriteSearch() {
        prefsManager.favoriteSearchCounter += 1

        if (prefsManager.favoriteSearchCounter == 3) {
            checkIfReviewShouldPrompt(true)
        }
    }

    fun favoriteProperty() {
        prefsManager.favoritePropertyCounter += 1

        if (prefsManager.favoritePropertyCounter == 3) {
            checkIfReviewShouldPrompt(false)
        }
    }

    fun detailViewSeen() {
        prefsManager.propertyDetailsOpenedCounter += 1

        if (prefsManager.propertyDetailsOpenedCounter == 20) {
            checkIfReviewShouldPrompt(true)
        }
    }

    fun afterFiveMinutes() {
        checkIfReviewShouldPrompt(false)
    }

    fun reviewGiven() {
        prefsManager.reviewGiven = true
    }

    private fun checkIfReviewShouldPrompt(isModal: Boolean) {

        val lastSeen = prefsManager.lastReviewDialogSeen.toDate()
        val today = Date()

        val lastAskedWithin30Days = when (BuildConfig.DEBUG) {
            false -> lastSeen in 30.days.ago..today
            // true -> lastSeen in 30.days.ago..today
            true -> lastSeen in 5.minute.ago..today // change to check every 5 minutes instead of 30 when testing
        }

        if (!prefsManager.reviewGiven && !lastAskedWithin30Days && prefsManager.reviewDialogSeenAmount < 3) {
            // lastSeen is not in the last 30 days and have not been seen more than 3 times
            if (isModal) {
                prefsManager.reviewShouldShow = true
            } else {
                //NavManager.openModally(Routing.ReviewDialog())
            }

            prefsManager.lastReviewDialogSeen = today.time
            prefsManager.reviewDialogSeenAmount += 1
        }
    }

    fun reset() {
        prefsManager.favoriteSearchCounter = 0
        prefsManager.favoritePropertyCounter = 0
        prefsManager.propertyDetailsOpenedCounter = 0
    }
}
