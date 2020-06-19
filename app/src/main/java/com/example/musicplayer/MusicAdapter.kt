package com.example.musicplayer

import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayInputStream
import java.io.InputStream


public class MusicAdapter(
        private var mfiles : ArrayList<MusicFiles> ,
        private var clickListener : onMusicItemClickListener
) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {


    var context : Context? = null

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        var fileName : TextView = itemView.findViewById(R.id.music_file)
        var fileImag : ImageView = itemView.findViewById(R.id.music_img)

        fun initialized(item : MusicFiles , action : onMusicItemClickListener) {
            fileName.text = item.getTitle()
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(item.getPath())
            val art = retriever.embeddedPicture
            if (art != null) {

                val kkdkd : InputStream = ByteArrayInputStream(retriever.embeddedPicture)
                val bm = BitmapFactory.decodeStream(kkdkd)
                fileImag.setImageBitmap(bm)
            } else {
                fileImag.setImageResource(R.drawable.index)
            }

            itemView.setOnClickListener {
                action.onItemClick(item , adapterPosition)
            }
        }


    }


    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : MusicAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.music_items , parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount() : Int {
        return try {
            mfiles.size
        } catch (e : Exception) {
            0
        }
    }

    override fun onBindViewHolder(holder : MusicAdapter.ViewHolder , position : Int) {

        holder.initialized(mfiles[position] , clickListener)
    }

    interface onMusicItemClickListener {
        fun onItemClick(item : MusicFiles , position : Int)
    }

}

