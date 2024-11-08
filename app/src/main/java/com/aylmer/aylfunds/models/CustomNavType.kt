package com.aylmer.aylfunds.models

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    val BudgetType = object : NavType<Long>(
        isNullableAllowed = false){

        override fun put(bundle: Bundle, key: String, value: Long) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun get(bundle: Bundle, key: String): Long? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Long {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Long): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }

}