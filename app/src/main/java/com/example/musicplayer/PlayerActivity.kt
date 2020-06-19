package com.example.musicplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.musicplayer.MainActivity.Companion.musicalbum
import com.example.musicplayer.MainActivity.Companion.repeatBoolean
import com.example.musicplayer.MainActivity.Companion.shuffleBoolean
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.lang.Exception
import kotlin.random.Random


private lateinit var songName : TextView
private lateinit var songArtist : TextView
private lateinit var startSong : TextView
private lateinit var endSong : TextView
private lateinit var song_Img : ImageView
private lateinit var shuffle : ImageView
private lateinit var repeat : ImageView
private lateinit var play_pause : FloatingActionButton
private lateinit var next_btt : ImageView
private lateinit var prev_btt : ImageView
private lateinit var seekbar : SeekBar
lateinit var handler : Handler
private var position : Int = 0
lateinit var noticationManager : NotificationManager
private var description = "Testing"
lateinit var notificationChannel : NotificationChannel
private lateinit var notifications : Notifications

class PlayerActivity : AppCompatActivity() {
    companion object {
        lateinit var listSongs : ArrayList<MusicFiles>
        lateinit var uri : Uri
        lateinit var mediaPlayer : MediaPlayer
         var kedo:MediaPlayer= MediaPlayer()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        songName = findViewById(R.id.song_name)
        songArtist = findViewById(R.id.artist)
        startSong = findViewById(R.id.start_song)
        endSong = findViewById(R.id.end_song)
        song_Img = findViewById(R.id.song_img)
        shuffle = findViewById(R.id.shuffle)
        repeat = findViewById(R.id.repeat)
        play_pause = findViewById(R.id.play_pause)
        next_btt = findViewById(R.id.next_bttn)
        prev_btt = findViewById(R.id.prev_btn)
        seekbar = findViewById(R.id.seekbar)
        seekbar = findViewById(R.id.seekbar)
        listSongs = musicalbum
        position = intent.getIntExtra("position" , - 1)

        uri = Uri.parse(listSongs.get(position).getPath())
        mediaPlayer = MediaPlayer()
        kedo= MediaPlayer()
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        mediaPlayer.isLooping=false
        mediaPlayer.setVolume(0.5f , 0.5f)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                    seekBar : SeekBar? ,
                    progress : Int ,
                    fromUser : Boolean
            ) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }

                mediaPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                    override fun onCompletion(mp : MediaPlayer?) {
                        try {
                            if (shuffleBoolean) {
                                play_pause.setImageResource(R.drawable.ic_baseline_pause_24)
                                mediaPlayer.start()
                            } else {
                                play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                                mediaPlayer.seekTo(0)
                                mediaPlayer.pause()
                            }

                        } catch (e : Exception) {

                        }
                    }

                })
            }

            override fun onStartTrackingTouch(seekBar : SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar : SeekBar?) {

            }

        })
        onProcess()
    }
    fun onProcess() {
        mediaPlayer = MediaPlayer.create(this , uri)



        getImage(uri)
        songName.text = listSongs.get(position).getTitle()
        songArtist.text = listSongs.get(position).getArtist()
        endSong.text = formattedTime(mediaPlayer.duration)
        seekbar.max = mediaPlayer.duration
        Thread(Runnable {
            while (true) {

                try {
                    var mssg = Message()
                    mssg.what = mediaPlayer.currentPosition
                    handler.sendMessage(mssg)
                    Thread.sleep(1000)

                } catch (e : Exception) {

                }
            }
        }).start()

        @SuppressLint("HandlerLeak")
        handler = object : Handler() {

            override fun handleMessage(msg : Message) {
                var currentPos = msg.what
                seekbar.progress = currentPos
                var elapsedTime = formattedTime(currentPos)
                startSong.text = elapsedTime


            }
        }

    }

    private fun formattedTime(currentPos : Int) : String {
        var timelabel : String
        val min = currentPos / 1000 / 60
        val sec = currentPos / 1000 % 60
        timelabel = "$min:"
        if (sec < 10) {
            timelabel += "0"
            timelabel += sec
        } else {
            timelabel += sec
        }
        return timelabel

    }

    fun getImage(uri : Uri) {

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(uri.toString())
        val art = retriever.embeddedPicture
        if (art != null) {

            val kkdkd : InputStream = ByteArrayInputStream(retriever.embeddedPicture)
            val bm = BitmapFactory.decodeStream(kkdkd)
            song_Img.setImageBitmap(bm)
        } else {
            song_Img.setImageResource(R.drawable.index)
        }
    }

    fun next_bttn(view : View) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.release()
        }

        if (shuffleBoolean && ! repeatBoolean) {
            position = getRandom(listSongs.size - 1)

        } else if (! shuffleBoolean && repeatBoolean) {


        } else if (shuffleBoolean && repeatBoolean) {

        } else {
            position = ((position + 1) % listSongs.size)
        }

        play_pause.setImageResource(R.drawable.ic_baseline_pause_24)


        uri = Uri.parse(listSongs.get(position).getPath())
        mediaPlayer = MediaPlayer.create(applicationContext , uri)
        getImage(uri)
        onProcess()

        mediaPlayer.start()
    }

    private fun getRandom(i : Int) : Int {
        val random = Random
        return random.nextInt(i + 1)

    }

    fun prev_bttn(view : View) {
        mediaPlayer.pause()
        play_pause.setImageResource(R.drawable.ic_baseline_pause_24)

        position = (position - 1)
        if (position < 0) {
            position = listSongs.size - 1
        } else {
            position - 1
        }
        uri = Uri.parse(listSongs.get(position).getPath())
        mediaPlayer = MediaPlayer.create(applicationContext , uri)
        getImage(uri)
        onProcess()
        mediaPlayer.start()
    }

    fun ShuffleBttn(view : View) {

        if (shuffleBoolean) {
            shuffleBoolean = false
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24)
            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                        seekBar : SeekBar? ,
                        progress : Int ,
                        fromUser : Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress)
                    }

                    mediaPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                        override fun onCompletion(mp : MediaPlayer?) = try {
                            play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                            mediaPlayer.seekTo(0)
                            mediaPlayer.pause()
                        } catch (e : Exception) {


                        }

                    })

                }

                override fun onStartTrackingTouch(seekBar : SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar : SeekBar?) {

                }

            })


        } else {
            shuffleBoolean = true
            seekbar.max = mediaPlayer.duration

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                        seekBar : SeekBar? ,
                        progress : Int ,
                        fromUser : Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress)
                    }

                    mediaPlayer.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
                        override fun onCompletion(mp : MediaPlayer?) = try {

                            position = getRandom(listSongs.size - 1)

                            uri = Uri.parse(listSongs.get(position).getPath())
                            mediaPlayer = MediaPlayer.create(applicationContext , uri)
                            getImage(uri)
                            onProcess()
                            play_pause.setImageResource(R.drawable.ic_baseline_pause_24)
                            mediaPlayer.seekTo(0)
                            mediaPlayer.start()
                        } catch (e : Exception) {


                        }

                    })

                }

                override fun onStartTrackingTouch(seekBar : SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar : SeekBar?) {

                }

            })
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_glitter)

        }
    }

    fun RepeatBttn(view : View) {
        if (repeatBoolean) {
            repeatBoolean = false
            repeat.setImageResource(R.drawable.ic_baseline_repeat_24)
            mediaPlayer.isLooping = false
        } else {
            repeatBoolean = true
            repeat.setImageResource(R.drawable.ic_baseline_repeat_one_24)
            mediaPlayer.isLooping = true
        }

    }

    fun play_btn(view : View) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()

            play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        } else {
            play_pause.setImageResource(R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()

            noticationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                        Notifications.CHANNELID ,
                        description ,
                        NotificationManager.IMPORTANCE_HIGH
                )

                    noticationManager.createNotificationChannel(notificationChannel)
                notifications = Notifications()
                notifications.createChannel(
                        this ,
                        musicalbum ,
                        position ,
                        R.drawable.ic_baseline_pause_24
                )

            }


        }


    }

    fun backbttn(view : View) {
        startActivity(Intent(this , MainActivity::class.java))
        kedo= mediaPlayer
    }

    override fun onRestart() {
        if(mediaPlayer.isPlaying){
            mediaPlayer.release()
            mediaPlayer.stop()
        }
        super.onRestart()
    }


}