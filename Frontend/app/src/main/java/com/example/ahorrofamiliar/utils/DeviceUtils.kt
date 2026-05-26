package com.example.ahorrofamiliar.utils

import android.content.Context
import java.util.UUID

object DeviceUtils {

    fun obtenerDispositivoId(
        context: Context
    ): String {

        val prefs = context.getSharedPreferences(
            "app",
            Context.MODE_PRIVATE
        )

        var id = prefs.getString(
            "device_id",
            null
        )

        if (id == null) {

            id = UUID.randomUUID().toString()

            prefs.edit()
                .putString("device_id", id)
                .apply()

        }

        return id
    }
}