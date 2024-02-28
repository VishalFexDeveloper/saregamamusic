package com.example.entertainment.MainFragment

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.entertainment.Adapter.ViedeoViewPager2Adapter
import com.example.entertainment.R
import com.example.entertainment.VideoFragmentChild.VideosFolderFragment
import com.example.entertainment.VideoFragmentChild.VideosListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class VideoFragmentParent : Fragment() {

    lateinit var viewPager2: ViewPager2
    lateinit var tabLayout: TabLayout
    private lateinit var viewPager2Adapter: ViedeoViewPager2Adapter


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layoutview = inflater.inflate(R.layout.fragment_video_parent, container, false)

        viewPager2 = layoutview.findViewById(R.id.videoViewPager2)
        tabLayout = layoutview.findViewById(R.id.videoTabs)


        viewPager2Adapter = ViedeoViewPager2Adapter(this.childFragmentManager,lifecycle)

        viewPager2.adapter = viewPager2Adapter

        TabLayoutMediator(tabLayout,viewPager2){tab,position ->
            when(position){

                0 -> {
                    tab.text = "Videos"
                }

                1 ->{
                    tab.text = "Folders"

                }

            }
        }.attach()






        return layoutview

    }






}