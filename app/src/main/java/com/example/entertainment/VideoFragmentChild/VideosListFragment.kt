package com.example.entertainment.VideoFragmentChild

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.entertainment.Adapter.VideostartAdapter
import com.example.entertainment.MainActivity
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.MainFragment.VideoFragmentParent
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.R

class VideosListFragment : Fragment() {

    lateinit var videosAdapter: VideostartAdapter
    lateinit var totalVideos: TextView
    lateinit var mutableList: MutableList<VideoDataMadal>
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar

    companion object{
        lateinit var videoRecyclerView: RecyclerView
        var checkGrid:Boolean = false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val videoview = inflater.inflate(R.layout.fragment_videos_list, container, false)

        videoRecyclerView = videoview.findViewById(R.id.videoListRecyclerView)
        totalVideos = videoview.findViewById(R.id.total_Videos)
        toolbar = videoview.findViewById(R.id.toolbarMenu)


        mutableList = mutableListOf()
        mutableList.addAll(MusicFragmentParent.videoList)

        totalVideos.text = " ${mutableList.size} Videos"

        videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        videosAdapter = VideostartAdapter(requireContext(), mutableList, isGrid = false)
        videoRecyclerView.adapter = videosAdapter


        toolbar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.sortOrder ->{
                    if (checkGrid){
                        videosAdapter = VideostartAdapter(requireContext(), mutableList, isGrid = false)
                        videoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                        listRecycler()
                        checkGrid = false
                        videoRecyclerView.adapter = videosAdapter

                    }else{
                        videosAdapter = VideostartAdapter(requireContext(), mutableList, isGrid = true)
                        videoRecyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                        gridRecycler()
                        checkGrid = true
                        videoRecyclerView.adapter = videosAdapter
                    }
                }

            }
            true
        }


        return videoview
    }

    override fun onResume() {
        super.onResume()
        videosAdapter.refreshData(MusicFragmentParent.videoList)
    }

    fun listRecycler() {
        toolbar.menu.findItem(R.id.sortOrder)?.setIcon(R.drawable.icon_grid)
    }

    fun gridRecycler() {
        toolbar.menu.findItem(R.id.sortOrder)?.setIcon(R.drawable.icons8_list_23)
    }


}