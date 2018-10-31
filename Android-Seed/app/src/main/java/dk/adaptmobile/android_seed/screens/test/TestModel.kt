package dk.adaptmobile.android_seed.screens.test

import dk.adaptmobile.android_seed.model.TestResponse
import dk.adaptmobile.android_seed.network.ConnectionManager
import io.reactivex.Observable

/**
 * Created by Alex on 5/9/18
 */
class TestModel {

    fun test(): Observable<TestResponse> {
        return ConnectionManager.restService.getRequest()
    }

}