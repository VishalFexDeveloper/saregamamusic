package com.example.entertainment.MusicFragmentChild

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.AlbumsListAdapter
import com.example.entertainment.MainActivity
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.AlbumsData
import com.example.entertainment.R

class AlbumFragment : Fragment() {


    lateinit var albumsRecyclerView: RecyclerView
    lateinit var customsListAdapter: AlbumsListAdapter

    companion object{

        lateinit var albumsList: ArrayList<AlbumsData>

    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val albumsLayout = inflater.inflate(R.layout.fragment_album, container, false)

        albumsRecyclerView = albumsLayout.findViewById(R.id.albums_RecyclerView_music)
        albumsList = ArrayList()


        albumsList.addAll(MusicFragmentParent.albumsList) // Add all albums to the albumsList

        albumsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        customsListAdapter = AlbumsListAdapter(requireContext(), albumsList)

        albumsRecyclerView.adapter = customsListAdapter


        return albumsLayout
    }








}