package com.example.ahorrofamiliar.util

import android.content.Context
import java.util.UUID

/**
 * Utilidades relacionadas con el dispositivo físico.
 */
object DeviceUtils {

    /**
     * Obtiene un identificador único para el dispositivo.
     * Si no existe uno guardado, genera un UUID nuevo y lo almacena en SharedPreferences.
     *
     * @param context El contexto de la aplicación para acceder a SharedPreferences.
     * @return Un String que representa el ID único del dispositivo.
     */
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