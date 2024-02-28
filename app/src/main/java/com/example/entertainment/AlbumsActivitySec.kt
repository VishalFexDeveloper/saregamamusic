package com.example.entertainment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.entertainment.Adapter.MusicAdapter
import com.example.entertainment.ModalData.Music
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.MusicFragmentChild.AlbumFragment
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.lang.Exception

class AlbumsActivitySec : AppCompatActivity() {

    lateinit var backBtn: ShapeableImageView
    lateinit var albumName: TextView
    lateinit var songcont: TextView
    lateinit var albumImg: ShapeableImageView
    lateinit var albumsRecyclerView: RecyclerView

    companion object {
        lateinit var albumList: ArrayList<Music>

    }

    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_albums_sec)

        setTheme(R.style.albumPlayerActivityTheme)

        backBtn = findViewById(R.id.albumsBackBtnSec)
        albumName = findViewById(R.id.albumsNameSongSec)
        songcont = findViewById(R.id.albumCountSongSec)
        albumImg = findViewById(R.id.albumsImgSec)
        albumsRecyclerView = findViewById(R.id.albums_RecyclerView_Sec)
        albumList = ArrayList()



        backBtn.setOnClickListener {
            finish()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.show(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val position = intent.getIntExtra("position", 0)



        albumName.text = AlbumFragment.albumsList[position].name
        songcont.text = AlbumFragment.albumsList[position].albumsCount


        Glide.with(this)
            .load(AlbumFragment.albumsList[position].albumIcon)
            .placeholder(R.drawable.moodaaaaaaaaaaaaaa) // Placeholder image
            .into(albumImg)

        albumList.addAll(getAllAlbumInSong(AlbumFragment.albumsList[position].albumId))

        albumsRecyclerView.layoutManager = LinearLayoutManager(this)
        albumsRecyclerView.adapter = MusicAdapter(this, albumList, isFolder = 2)


    }


    // create fun get all albums in item song
    @SuppressLint("Range")
    private fun getAllAlbumInSong(albumId: String): ArrayList<Music> {
        val temList = ArrayList<Music>()

        val selection = MediaStore.Audio.Media.ALBUM_ID + " like? "

        val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " ASC"
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

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(albumId),
            sortOrder
        )
        if (cursor != null) {
            if (cursor.moveToNext()) {
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))

                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))

                    val albumsC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))

                    val artistsC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))


                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))

                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    //folder data fragment all data
                    val folderC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.BUCKET_ID))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))


                    try {

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


                    } catch (_: Exception) {
                    }

                } while (cursor.moveToNext())
                cursor.close()
            }
        }

        return temList
    }


}