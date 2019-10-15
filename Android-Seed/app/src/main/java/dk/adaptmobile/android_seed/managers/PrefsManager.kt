package dk.adaptmobile.android_seed.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object PrefsManager {

    private lateinit var preferences: SharedPreferences

//    var key: String? by PreferenceFieldDelegate.String("KEY", "")

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun clear() {
    }

    private sealed class PreferenceFieldDelegate<T>(protected val key: kotlin.String, protected val default: T) : ReadWriteProperty<PrefsManager, T> {

        class Boolean(key: kotlin.String, default: kotlin.Boolean = false) : PreferenceFieldDelegate<kotlin.Boolean>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getBoolean(key, default)
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: kotlin.Boolean) = thisRef.preferences.edit().putBoolean(key, value).apply()
        }

        class Int(key: kotlin.String, default: kotlin.Int = 0) : PreferenceFieldDelegate<kotlin.Int>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getInt(key, default)
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: kotlin.Int) = thisRef.preferences.edit().putInt(key, value).apply()
        }

        class String(key: kotlin.String, default: kotlin.String = "") : PreferenceFieldDelegate<kotlin.String?>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getString(key, default) ?: ""
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: kotlin.String?) = thisRef.preferences.edit().putString(key, value).apply()
        }

        class Float(key: kotlin.String, default: kotlin.Float = 0f) : PreferenceFieldDelegate<kotlin.Float>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getFloat(key, default)
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: kotlin.Float) = thisRef.preferences.edit().putFloat(key, value).apply()
        }

        class Long(key: kotlin.String, default: kotlin.Long = 0L) : PreferenceFieldDelegate<kotlin.Long>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getLong(key, default)
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: kotlin.Long) = thisRef.preferences.edit().putLong(key, value).apply()
        }

        class StringSet(key: kotlin.String, default: Set<kotlin.String> = emptySet()) : PreferenceFieldDelegate<Set<kotlin.String>>(key, default) {
            override fun getValue(thisRef: PrefsManager, property: KProperty<*>) = thisRef.preferences.getStringSet(key, default) ?: emptySet()
            override fun setValue(thisRef: PrefsManager, property: KProperty<*>, value: Set<kotlin.String>) = thisRef.preferences.edit().putStringSet(key, value).apply()
        }
    }
}
