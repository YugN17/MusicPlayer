package com.example.musicplayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationAcitonService : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val intent=Intent("Trcks_Tracks").putExtra("hello",intent?.action)

        context?.sendBroadcast(intent)
    }


}