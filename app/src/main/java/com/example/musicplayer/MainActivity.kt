package com.example.musicplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var tempAudioList : ArrayList<MusicFiles>
    private lateinit var temporary : ArrayList<MusicFiles>
    private var REQUEST_CODE = 1

    companion object {
        lateinit var musicalbum : ArrayList<MusicFiles>
        var shuffleBoolean : Boolean = false
        var repeatBoolean : Boolean = false
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bundle = Bundle()
        val fragment1 = Fragment1()
        musicalbum = ArrayList<MusicFiles>()
        temporary = ArrayList<MusicFiles>()

        tempAudioList = ArrayList<MusicFiles>()
        permission()
        tempAudioList = musicalbum
        bundle.putParcelableArrayList("hello" , musicalbum)
        fragment1.arguments = bundle

    }
    private fun permission() {

        if (ContextCompat.checkSelfPermission(
                applicationContext ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this@MainActivity ,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) ,
                REQUEST_CODE
            )
        } else {
            musicalbum = getAllAudio(this)
        }

    }

    private fun getAllAudio(context : Context) : ArrayList<MusicFiles> {


        val uri : Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media.ALBUM ,
            MediaStore.Audio.Media.TITLE ,
            MediaStore.Audio.Media.DURATION ,
            MediaStore.Audio.Media.DATA ,
            MediaStore.Audio.Media.ARTIST
        )

        val cursor = context.contentResolver.query(uri , projection , null , null , null)
        if (cursor != null) {

            while (cursor.moveToNext()) {

                val album = cursor.getString(0)
                val title = cursor.getString(1)
                val duration = cursor.getString(2)
                val path = cursor.getString(3)
                val artist = cursor.getString(4)


//                this.musicFiles=musicFiles
                Log.e("Path:" + path , "Album:" + album)


                tempAudioList.add(MusicFiles(path , title , artist , album , duration))

//                loadData()
                Log.e("tempaudio" + tempAudioList.size , "kksdmk")

            }
            cursor.close()


        }

        return tempAudioList
    }

    override fun onRequestPermissionsResult(
            requestCode : Int ,
            permissions : Array<out String> ,
            grantResults : IntArray
    ) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this , "Permission Granted" , Toast.LENGTH_SHORT).show()
                musicalbum = getAllAudio(this)
            } else {
                ActivityCompat.requestPermissions(
                    this ,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) ,
                    REQUEST_CODE
                )

            }
        }
    }

    override fun onCreateOptionsMenu(menu : Menu?) : Boolean {
        menuInflater.inflate(R.menu.menu , menu)

        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                val fragment1 = Fragment1.adapter
                fragment1.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}