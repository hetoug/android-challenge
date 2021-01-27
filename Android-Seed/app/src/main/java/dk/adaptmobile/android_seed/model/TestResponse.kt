package dk.adaptmobile.android_seed.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestResponse(val slideshow: Slideshow)

@JsonClass(generateAdapter = true)
data class Slideshow(val title: String)