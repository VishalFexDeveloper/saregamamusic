package com.example.entertainment.MainFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.entertainment.Adapter.viewPagerAdapter2
import com.example.entertainment.ModalData.AlbumsData
import com.example.entertainment.ModalData.FolderData
import com.example.entertainment.ModalData.FolderVideo
import com.example.entertainment.ModalData.Music
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.R
import com.example.entertainment.SearchActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.lang.Exception

class MusicFragmentParent<Toolbar> : Fragment() {




    companion object {
        lateinit var videoList: MutableList<VideoDataMadal>
        lateinit var musicList: MutableList<Music>
        lateinit var foldersVideoList: MutableList<FolderVideo>
        lateinit var musicFolderList: MutableList<FolderData>
        lateinit var albumsList: MutableList<AlbumsData>
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutview = inflater.inflate(R.layout.fragment_music_parent, container, false)

        val tableLayout = layoutview.findViewById<TabLayout>(R.id.tabs)
        val viewpaper2 = layoutview.findViewById<ViewPager2>(R.id.viewpager2)
        val toolbar:androidx.appcompat.widget.Toolbar =layoutview.findViewById(R.id.toolbarMusic)


            foldersVideoList = ArrayList()
            videoList = getAllVideo()
            musicList = getAllAudio()
            musicFolderList = getAllDisplayFolders()
            albumsList = getAllAlbums()


        val countSong = musicList.size
        val countFolder = musicFolderList.size
        val countAlbums = albumsList.size



        val viewPagerAdapter = viewPagerAdapter2(this.childFragmentManager, lifecycle)
        viewpaper2.adapter = viewPagerAdapter
        TabLayoutMediator(tableLayout, viewpaper2) { tab, position ->

            when (position) {
                0 -> {
                    if (countSong != 0){
                        tab.text = "Songs $countSong"
                    }else{
                        tab.text = "Songs"
                    }

                }

                1 -> {
                    tab.text = "Playlists"
                }

                2 -> {
                    if (countFolder != 0){
                        tab.text = "Folders $countFolder"
                    }else{
                        tab.text = "Folders"
                    }

                }

                3 -> {
                    if (countAlbums != 0){
                        tab.text = "Albums $countAlbums"
                    }else{
                        tab.text = "Albums"
                    }
                }
            }

        }.attach()


        toolbar.setOnMenuItemClickListener {
            when(it.itemId){

                R.id.searchPage ->{

                    startActivity(Intent(requireContext(),SearchActivity::class.java))
                    return@setOnMenuItemClickListener true
                }

                else ->{
                    return@setOnMenuItemClickListener false
                }
            }
        }

        return layoutview

    }




    // create fun get all videos phone
    @SuppressLint("Range")
    private fun getAllVideo(): ArrayList<VideoDataMadal> {
        val temList = ArrayList<VideoDataMadal>()

        val temFolderList = ArrayList<String>()

        val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
        val projection = arrayOf(
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_ID
        )

        val cursor = requireContext().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )
        if (cursor != null) {
            if (cursor.moveToNext()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))

                    val folderC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderidC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))

                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))

                    try {
                        val file = File(pathC)
                        val artUriC = Uri.fromFile(file)
                        val video = VideoDataMadal(
                            id = idC,
                            title = titleC,
                            folder = folderC,
                            duration = durationC,
                            size = sizeC,
                            path = pathC,
                            artUri = artUriC
                        )
                        if (file.exists()) temList.add(video)

                        //for adding folders
                        if (!temFolderList.contains(folderC)) {
                            temFolderList.add(folderC)
                            val videoCount = countVideosInFolder(folderidC)

                            if (videoCount == 1) {
                                foldersVideoList.add(
                                    FolderVideo(
                                        id = folderidC,
                                        name = folderC,
                                        videoCount = "$videoCount Video",
                                        img = artUriC
                                    )
                                )
                            } else {
                                foldersVideoList.add(
                                    FolderVideo(
                                        id = folderidC,
                                        name = folderC,
                                        videoCount = "$videoCount Videos",
                                        img = artUriC
                                    )
                                )
                            }
                        }

                    } catch (_: Exception) {
                    }

                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return temList
    }




    // create fun folder video in count videos
    private fun countVideosInFolder(folderId: String): Int {
        val selection = "${MediaStore.Video.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(folderId)
        val projection = arrayOf(
            MediaStore.Video.Media._ID
        )

        val cursor = requireContext().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val videoCount = cursor?.count ?: 0
        cursor?.close()
        return videoCount
    }




    // create fun get all music phone
    @SuppressLint("Range", "Recycle")
    private fun getAllAudio(): ArrayList<Music> {

        val tempList = ArrayList<Music>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.BUCKET_ID,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.RELATIVE_PATH,
        )

        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        val cursor = requireContext().contentResolver.query(
            uri,
            projection,
            selection,
            null,
            sortOrder
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))

                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))

                    val albumsC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))

                    val artistsC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))


                    val durationC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    //folder data fragment all data
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))

                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))

                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))


                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()
                    val music = Music(
                        id = idC,
                        title = titleC,
                        album = albumsC,
                        artist = artistsC,
                        duration = durationC,
                        path = pathC,
                        folderC = folderC,
                        folderIdC = folderIdC,
                        artUri = artUriC
                    )

                    val file = File(music.path)
                    if (file.exists()) {
                        tempList.add(music)
                    }


                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return tempList
    }



    // create fun get all folder fun
    @SuppressLint("Range", "Recycle")
    private fun getAllDisplayFolders(): MutableList<FolderData> {
        val projection = arrayOf(
            MediaStore.Audio.Media.BUCKET_ID,
            MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Audio.Media.DATA,
        )

        val folders = mutableListOf<FolderData>()

        val cursor: Cursor? = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Audio.Media.BUCKET_DISPLAY_NAME} DESC"
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                val name = it.getString(it.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
                val path = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))

                // Get the number of songs in the folder by counting the files with the same parent
                var count = countSongsInFolder(id)

                // Add the folder to the list if it is not already there
                if (!folders.any { folderData -> folderData.id == id }) {
                    val absolutePath = File(path).absolutePath

                    if (count == 1) {
                        folders.add(
                            FolderData(
                                id = id,
                                folderName = name,
                                Countandfile = "$count song - $absolutePath "
                            )
                        )
                    } else {
                        folders.add(
                            FolderData(
                                id = id,
                                folderName = name,
                                Countandfile = "$count songs - $absolutePath "
                            )
                        )
                    }
                }
            }
        }

        return folders
    }





    // create fun folder in item songs
    private fun countSongsInFolder(folderId: String): Int {
        val selection = "${MediaStore.Audio.Media.BUCKET_ID} = ?"
        val selectionArgs = arrayOf(folderId)
        val projection = arrayOf(MediaStore.Audio.Media._ID)

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val songCount = cursor?.count ?: 0
        cursor?.close()
        return songCount
    }




    //create fun  get all albums folder
    @SuppressLint("Range")
    fun getAllAlbums(): MutableList<AlbumsData> {

        val albums = mutableListOf<AlbumsData>()

        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        // Define the columns to query from the media store
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.ALBUM_ID,


            )
        val sortOrder = "${MediaStore.Audio.Albums.ALBUM} ASC"
        // Use a content resolver to query the media store
        requireContext().contentResolver.query(
            uri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            // Loop through the cursor and create AlbumData objects
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                val name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                val albumIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))


                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()

                val count = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))

                val counttext :Int = count.toInt()

                if (counttext == 1){
                    val albumsData = AlbumsData(id = id, name = name,albumId = albumIdC , albumIcon = artUriC, albumsCount = "$count song")
                    albums.add(albumsData)
                }else{
                    val albumsData = AlbumsData(id = id, name = name,albumId = albumIdC , albumIcon = artUriC, albumsCount = "$count songs")
                    albums.add(albumsData)
                }

            }
        }

        return albums
    }


    override fun onResume() {
        super.onResume()

    }



}









