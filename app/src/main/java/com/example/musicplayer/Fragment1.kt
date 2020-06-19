package com.example.musicplayer
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.MainActivity.Companion.musicalbum

class Fragment1 : Fragment() , MusicAdapter.onMusicItemClickListener {

    private lateinit var recyclerView : RecyclerView

    companion object {
        lateinit var adapter : MusicAdapter
    }

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_1 , container , false)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = MusicAdapter(musicalbum , this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onItemClick(item : MusicFiles , position : Int) {
        if(PlayerActivity.kedo.isPlaying){
            PlayerActivity.kedo.stop()
            PlayerActivity.kedo.release()
        }
        val intent = Intent(context , PlayerActivity::class.java)
        intent.putExtra("position" , position)
        startActivity(intent)

    }

}