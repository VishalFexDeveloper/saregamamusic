package com.example.entertainment.MusicFragmentChild

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.FolderAdapterMusic
import com.example.entertainment.MainActivity
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.AlbumsData
import com.example.entertainment.ModalData.FolderData
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.R


class FolderFragmentMusic : Fragment() {



    lateinit var recyclerView: RecyclerView
    lateinit var customAdapterMusic: FolderAdapterMusic
    lateinit var folderList: MutableList<FolderData>



    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layoutView = inflater.inflate(R.layout.fragment_folder_music, container, false)

        recyclerView = layoutView.findViewById(R.id.folder_RecyclerView_music)

        folderList = mutableListOf()
        folderList.addAll(MusicFragmentParent.musicFolderList)

        customAdapterMusic = FolderAdapterMusic(requireContext(), folderList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = customAdapterMusic

        customAdapterMusic.notifyDataSetChanged()



        return layoutView
    }






}