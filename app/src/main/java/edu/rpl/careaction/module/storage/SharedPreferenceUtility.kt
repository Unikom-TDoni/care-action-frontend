package edu.rpl.careaction.module.storage

import android.content.Context
import java.lang.IllegalArgumentException

abstract class SharedPreferenceUtility<T : Enum<T>>(
    private val name: String,
    private val context: Context
) {
    protected val sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)!!
    private val editor = sharedPreferences.edit()

    fun <TValue> store(keyValueCollection: Map<T, TValue>) {
        keyValueCollection.forEach { (key, value) ->
            when (value) {
                is Int -> editor.putInt(key.name, value)
                is Long -> editor.putLong(key.name, value)
                is Float -> editor.putFloat(key.name, value)
                is String -> editor.putString(key.name, value)
                is Boolean -> editor.putBoolean(key.name, value)
                else -> throw IllegalArgumentException("Type not supported by shared preferences")
            }
        }
        editor.apply()
    }

    fun clear() {
        editor.clear()
        editor.apply()
    }

    fun remove(keyCollection: Set<T>) {
        for (key in keyCollection)
            editor.remove(key.name)
        editor.apply()
    }

    fun isExist(key: T)
        = sharedPreferences.contains(key.name)
}