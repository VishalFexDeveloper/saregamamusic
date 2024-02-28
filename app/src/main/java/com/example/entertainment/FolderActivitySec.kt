package com.example.entertainment

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.MusicAdapter
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.Music
import com.example.entertainment.ModalData.VideoDataMadal
import com.google.android.material.imageview.ShapeableImageView
import java.io.File

class FolderActivitySec : AppCompatActivity() {

    lateinit var foldersRecyclerSec :RecyclerView
    lateinit var backButton:ShapeableImageView

    companion object{
        lateinit var folderList : ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_sec)

        foldersRecyclerSec = findViewById(R.id.foldersRecyclerSec)
        backButton = findViewById(R.id.foldersBackBtnSec)
        folderList = arrayListOf()

        
        val position = intent.getIntExtra("position", 0)

        if (position < MusicFragmentParent.musicFolderList.size) {
            folderList.addAll(getAllItem(MusicFragmentParent.musicFolderList[position].id))
        } else {
            // Handle the case where MusicFragmentParent.musicFolderList is null or the position is out of bounds.
        }


        foldersRecyclerSec.layoutManager = LinearLayoutManager(this)

        foldersRecyclerSec.adapter =  MusicAdapter(this, folderList,isFolder = 1)

        backButton.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("Range", "Recycle")
    private fun getAllItem(bUCKET_ID :String): ArrayList<Music> {

        val temList = ArrayList<Music>()

        val selection = MediaStore.Audio.Media.BUCKET_ID + " like? "

        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " ASC"


        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI


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


        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            arrayOf(bUCKET_ID),
            sortOrder
        )

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val titleC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))

                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))

                    val albumsC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))

                    val artistsC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))


                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val albumIdC =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                            .toString()
                    //folder data fragment all data
                    val folderC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
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
                        temList.add(music)
                    }

                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return temList
    }
}


