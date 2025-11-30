package com.example.astucianaval.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.astucianaval.notifications.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        val message = intent?.getStringExtra("msg")
            ?: "¡Es hora de jugar! Bienvenido de nuevo, capitán 😎🚢"

        NotificationHelper.showNotification(
            context,
            "Astucia Naval",
            message
        )
    }
}

