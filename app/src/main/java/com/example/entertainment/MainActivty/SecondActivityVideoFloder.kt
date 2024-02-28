package com.example.entertainment.MainActivty

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.entertainment.Adapter.VideostartAdapter
import com.example.entertainment.MainActivity
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.R
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.lang.Exception

class SecondActivityVideoFloder : AppCompatActivity() {
    private lateinit var backImage : ShapeableImageView
    private lateinit var folderTitle: TextView
    private lateinit var foldersRecyclerView: RecyclerView
    private lateinit var foldersListCount: TextView

    companion object{

        lateinit var currentFolderVideos: ArrayList<VideoDataMadal>

    }

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_video_floder)


        backImage = findViewById(R.id.folder_LayoutChildBack)
        folderTitle = findViewById(R.id.folder_NameSecondActivity)
        foldersRecyclerView = findViewById(R.id.folder_videoList)
        foldersListCount = findViewById(R.id.folder_videoCount)
        folderTitle.isSelected = true


        val position = intent.getIntExtra("position",0)

        val foldersTitle = MusicFragmentParent.foldersVideoList[position].name
        folderTitle.text = foldersTitle

        currentFolderVideos = arrayListOf()
        currentFolderVideos.addAll(getAllVideo(MusicFragmentParent.foldersVideoList[position].id))

        foldersRecyclerView.layoutManager = LinearLayoutManager(this@SecondActivityVideoFloder)
        foldersRecyclerView.adapter = VideostartAdapter(this@SecondActivityVideoFloder, currentFolderVideos, isFolder = true)

        foldersListCount.text = "${currentFolderVideos.size} Videos"

        backImage.setOnClickListener {
            onBackPressed()
        }


    }

    @SuppressLint("Range")
    private fun getAllVideo(folderId: String): ArrayList<VideoDataMadal> {
        val temList = ArrayList<VideoDataMadal>()

        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "

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

        val cursor = this.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf(folderId),
            sortOrder
        )
        if (cursor != null){
            if (cursor.moveToNext()){
                do{
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val sizeC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))

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


                    }catch (_: Exception){}

                }while (cursor.moveToNext())
                cursor.close()
            }
        }

        return temList
    }

}