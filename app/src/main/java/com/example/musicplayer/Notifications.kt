package com.example.musicplayer

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationManager
import android.content.Context
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.os.*
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.app.*
import android.support.v4.media.*

import androidx.core.app.NotificationCompat as MediaNotificationCompat

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.text.FieldPosition

//lateinit var pendingIntentprev: PendingIntent
//lateinit var pendingIntentnext: PendingIntent
//lateinit var pendingIntentpauss: PendingIntent
//lateinit var intent1: Intent
//lateinit var intent2: Intent
//lateinit var intent3: Intent
 class Notifications() {
    companion object {
        const val CHANNELID = "channel1"
        const val ACTIONPREVIOUS = "action previous"
        const val ACTIONNEXT = "action next"
        const val ACTIONPAUSE = "action pAUSE"
    }

    fun createChannel(context: Context, musicFiles: ArrayList<MusicFiles>, position: Int,playBtn:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationManagerCompat = NotificationManagerCompat.from(context)

            val mediaSessionCompat = MediaSessionCompat(context, "tag")
val ar:Bitmap
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(musicFiles.get(position).getPath())
            val art = retriever.embeddedPicture

            if (art != null) {

                val kkdkd: InputStream = ByteArrayInputStream(retriever.embeddedPicture)
                val bm = BitmapFactory.decodeStream(kkdkd)
                ar=bm
            }else{
                val bm = BitmapFactory.decodeResource(context.resources,R.drawable.index)
                ar=bm
            }
                val prevBtt = R.drawable.ic_baseline_skip_previous_24
                val nextBtt = R.drawable.ic_baseline_skip_next_24
                val paaaus = R.drawable.ic_baseline_pause_24

                val intent4 = Intent(context, PlayerActivity::class.java)
                val pending = PendingIntent.getActivity(
                    context,
                    0,
                    intent4,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val intent1 = Intent(context, NotificationAcitonService::class.java).setAction(ACTIONPREVIOUS)
                val pendingIntentpre = PendingIntent.getActivity(
                    context,
                    0,
                    intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )


                val intent2 = Intent(context, NotificationAcitonService::class.java).setAction(ACTIONPAUSE)
                val pendingIntentpauss = PendingIntent.getActivity(
                    context,
                    0,
                    intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val intent3 = Intent(context, NotificationAcitonService::class.java).setAction(ACTIONNEXT)
                val pendingIntentnext = PendingIntent.getActivity(
                    context,
                    0,
                    intent3,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notifications = NotificationCompat.Builder(context, CHANNELID)
                    .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                    .setContentTitle(musicFiles.get(position).getTitle())
                    .setContentText(musicFiles.get(position).getArtist())
                    .setLargeIcon(ar)
                    .addAction(
                        R.drawable.ic_baseline_skip_previous_24,
                        "previous",
                        pendingIntentpre
                    )
                    .addAction(playBtn, "pauseBtt", pendingIntentpauss)
                    .addAction(R.drawable.ic_baseline_skip_next_24, "next", pendingIntentnext)
                    .setStyle(
                        object : androidx.media.app.NotificationCompat.MediaStyle(){}.setShowActionsInCompactView(
                            0,1,2).setMediaSession(mediaSessionCompat.sessionToken))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .build()
                                notificationManagerCompat.notify(1,
                        notifications
                    )

        }

    }
}
