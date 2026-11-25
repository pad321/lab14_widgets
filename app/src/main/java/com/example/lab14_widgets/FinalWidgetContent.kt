package com.example.lab14_widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

val KEY_ACCIONES = intPreferencesKey("acciones_completadas")
val KEY_ULTIMA_ACCION = stringPreferencesKey("ultima_accion")

class FinalWidgetContent : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                DashboardWidgetUI()
            }
        }
    }
}

@Composable
private fun DashboardWidgetUI() {
    val prefs = currentState<Preferences>()
    val totalAcciones = prefs[KEY_ACCIONES] ?: 0
    val ultimaAccion = prefs[KEY_ULTIMA_ACCION] ?: "Sin actividad reciente"

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(20.dp)
            .background(Color(0xFF101828))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PANEL DE CONTROL",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF38BDF8)),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.defaultWeight())
            Text(
                text = "● ACTIVO ($totalAcciones)",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF4ADE80)),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(6.dp))

        Text(
            text = "Última acción: $ultimaAccion",
            style = TextStyle(
                color = ColorProvider(Color(0xFFE2E8F0)),
                fontSize = 12.sp
            )
        )

        Spacer(modifier = GlanceModifier.height(10.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                text = "+ Registrar Acción",
                onClick = actionRunCallback<RegistrarAccionCallback>(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorProvider(Color(0xFF0284C7)),
                    contentColor = ColorProvider(Color.White)
                )
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Button(
                text = "Reiniciar",
                onClick = actionRunCallback<ReiniciarAccionCallback>(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorProvider(Color(0xFF334155)),
                    contentColor = ColorProvider(Color.White)
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                text = "Abrir Home",
                onClick = actionStartActivity<MainActivity>(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorProvider(Color(0xFF1E293B)),
                    contentColor = ColorProvider(Color(0xFF38BDF8))
                )
            )
            Spacer(modifier = GlanceModifier.width(8.dp))
            Button(
                text = "Abrir Work",
                onClick = actionStartActivity<WorkActivity>(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = ColorProvider(Color(0xFF1E293B)),
                    contentColor = ColorProvider(Color(0xFF38BDF8))
                )
            )
        }
    }
}

class RegistrarAccionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val nuevoTotal = (prefs[KEY_ACCIONES] ?: 0) + 1
            prefs[KEY_ACCIONES] = nuevoTotal
            prefs[KEY_ULTIMA_ACCION] = "Tarea #$nuevoTotal completada"
        }
        FinalWidgetContent().update(context, glanceId)
    }
}

class ReiniciarAccionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[KEY_ACCIONES] = 0
            prefs[KEY_ULTIMA_ACCION] = "Contador reiniciado"
        }
        FinalWidgetContent().update(context, glanceId)
    }
}