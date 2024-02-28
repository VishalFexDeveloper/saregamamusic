package com.example.entertainment.MusicFragmentChild

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.MusicAdapter
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.Music
import com.example.entertainment.R
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView


class SongsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var parentAdapter: MusicAdapter
    lateinit var mutableList: MutableList<Music>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_songs, container, false)
//        fastScroller = layout.findViewById(R.id.fastScroller)

        mutableList = mutableListOf()
        mutableList.addAll(MusicFragmentParent.musicList)


        recyclerView = layout.findViewById(R.id.percent_RecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        parentAdapter = MusicAdapter(requireContext(), mutableList)

        recyclerView.adapter = parentAdapter


        return layout
    }

    override fun onResume() {
        super.onResume()
        parentAdapter.refreshDataMusic(MusicFragmentParent.musicList)
    }
}
