package com.example.musicplayer

import android.os.Parcel
import android.os.Parcelable
import java.time.Duration

class MusicFiles() :Parcelable{

  private lateinit var path:String

    private lateinit var title:String
   private lateinit var artist:String
    private lateinit var album:String
    private lateinit var duration:String

    constructor(parcel: Parcel) : this() {
        path = parcel.readString().toString()
        title = parcel.readString().toString()
        artist = parcel.readString().toString()
        album = parcel.readString().toString()
        duration = parcel.readString().toString()
    }


    constructor(path: String, title: String, artist: String, album: String, duration: String) : this() {
        this.path = path
        this.title = title
        this.artist = artist
        this.album = album
        this.duration = duration
    }

    fun getPath(): String {
        return path

    }
    fun setPath(path: String){
        this.path=path
    }
    fun getTitle():String{
        return title
    }
    fun getArtist():String{
        return artist
    }
    fun getAlbum():String{
        return album
    }
    fun getDuration():String{
        return duration
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeString(duration)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicFiles> {
        override fun createFromParcel(parcel: Parcel): MusicFiles {
            return MusicFiles(parcel)
        }

        override fun newArray(size: Int): Array<MusicFiles?> {
            return arrayOfNulls(size)
        }
    }


}