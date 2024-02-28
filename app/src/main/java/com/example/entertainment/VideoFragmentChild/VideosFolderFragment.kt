package com.example.entertainment.VideoFragmentChild

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.FolderAdapterVideo
import com.example.entertainment.MainActivity
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.FolderVideo
import com.example.entertainment.R


class VideosFolderFragment : Fragment() {
    lateinit var folderAdapter: FolderAdapterVideo

    lateinit var totalFolder: TextView

    companion object{

        lateinit var folderListVid: MutableList<FolderVideo>

        lateinit var folderRecyclerView: RecyclerView

    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val folderLayout = inflater.inflate(R.layout.fragment_videos_folder, container, false)

        folderRecyclerView = folderLayout.findViewById(R.id.folder_RecyclerView)
        totalFolder = folderLayout.findViewById(R.id.total_Folders)


        folderListVid = mutableListOf()
        folderListVid.addAll(MusicFragmentParent.foldersVideoList)

        folderAdapter = FolderAdapterVideo(requireContext(), folderListVid)

        totalFolder.text = " ${folderListVid.size} Folders"


        folderRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        folderRecyclerView.adapter = folderAdapter


        return folderLayout
    }




}