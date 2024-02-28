package com.example.entertainment

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.entertainment.Adapter.MusicService
import com.example.entertainment.Adapter.PlayList_Drop
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.Music
import com.example.entertainment.ModalData.formatDuration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import java.util.Collections

class PlayerActivity : AppCompatActivity(),ServiceConnection,MediaPlayer.OnCompletionListener {

    private lateinit var  backButton: ShapeableImageView
    private lateinit var songFolder: TextView
    private lateinit var prevBtn:ShapeableImageView
    private lateinit var nextBtn:ShapeableImageView
    private lateinit var shuffleMode: ShapeableImageView
    private lateinit var small_img_layout :LinearLayout
    private lateinit var  drop_up_List_Btn : ShapeableImageView
    private lateinit var tvSeekbarEnd :TextView


    companion object{
        lateinit var musicListPlayer : ArrayList<Music>
        var songPosition = 0
        var isplaying:Boolean = false
        var musicService:MusicService? = null
        lateinit var songtitle: TextView
        private var isPlaying = false
        lateinit var player_Image_small : ShapeableImageView
        lateinit var playPauseBtn : ExtendedFloatingActionButton
        lateinit var tvSeekbarStart :TextView
        lateinit var seekbarPa: AppCompatSeekBar

    }

    @SuppressLint("CheckResult", "MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setTheme(R.style.musicPlayer)
        // fo s
        backButton  = findViewById(R.id.back_button)
        songtitle  = findViewById(R.id.song_title_Player)
        songFolder = findViewById(R.id.song_folderName_Player)
        playPauseBtn = findViewById(R.id.playPauseBtnMusic)
        prevBtn = findViewById(R.id.prevBtnMusic)
        nextBtn = findViewById(R.id.nextBtnMusic)
        shuffleMode = findViewById(R.id.shuffleBtn_music)
        player_Image_small = findViewById(R.id.player_Image_small)
        small_img_layout = findViewById(R.id.small_img_layout)
        drop_up_List_Btn = findViewById(R.id.drop_up_Listview)
        seekbarPa = findViewById(R.id.seekbarPa)
        tvSeekbarStart = findViewById(R.id.tvSeekbarStart)
        tvSeekbarEnd = findViewById(R.id.tvSeekbarEnd)


        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)  // This line might be causing the issue

        initializeLayout()
        rotateSmallImageLayout()


        songtitle.isSelected = true

//        backButton.setOnTouchListener { v, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    // Change background color when pressed
//                    v.setBackgroundColor(Color.GREEN)
//                }
//                MotionEvent.ACTION_UP -> {
//                    // Restore original background color when released
//                    v.setBackgroundColor(Color.TRANSPARENT) // Set to original or desired color
//                    // Perform click action here if needed
//                }
//            }
//            // Return false to continue receiving touch events or true to consume them here
//            true
//        }


        backButton.setOnClickListener {
            onBackPressed()
        }


//        playPauseBtn.setOnClickListener {
//            if (isplaying) pauseMusic()
//            else playMusic()
//        }

        prevBtn.setOnClickListener {
            prevNextSong(increment = false)
        }

        nextBtn.setOnClickListener {
            prevNextSong(increment = true)
        }

        seekbarPa.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {


                if (fromUser){
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }

            }


            override fun onStartTrackingTouch(seekBar: SeekBar?)  = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

        })

        shuffleMode.setOnClickListener {
            musicListPlayer.shuffle()
        }

        drop_up_List_Btn.setOnClickListener {
            bottomSheet()
        }

    }





    private fun setLayout(){
        if (songPosition < musicListPlayer.size) {

            val imageList = arrayOf(
                R.drawable.live_music,
                R.drawable.img1, R.drawable.img2,
                R.drawable.img3, R.drawable.img5,
                R.drawable.music_logo
            )

            songtitle.text = musicListPlayer[songPosition].title
            songFolder.text = musicListPlayer[songPosition].album


            Glide.with(this)
                .load(musicListPlayer[songPosition].artUri)
                .placeholder(imageList.random())
                .into(player_Image_small)

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) {
                musicService!!.mediaPlayer = MediaPlayer()
            }
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPlayer[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isplaying = true
            playPauseBtn.setIconResource(R.drawable.pause_ic_icon32)
            tvSeekbarStart.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
            tvSeekbarEnd.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
            seekbarPa.progress = 0
            seekbarPa.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
        } catch (e: Exception) {
            // Handle exceptions (log or display an error message)
            e.printStackTrace()
        }
    }





    private fun initializeLayout() {
//        songPosition = intent.getIntExtra("position", 0)
        when (intent.getStringExtra("class")) {
            "allSong" -> {
                musicListPlayer = ArrayList()
                musicListPlayer.addAll(MusicFragmentParent.musicList)
                setLayout()
            }
            "allSongFolder" ->{
                musicListPlayer = ArrayList()
                musicListPlayer.addAll(FolderActivitySec.folderList)
                setLayout()
            }

            "allSongAlbum" ->{
                musicListPlayer = ArrayList()
                musicListPlayer.addAll(AlbumsActivitySec.albumList)
                setLayout()
            }
        }
    }


    private fun playMusic() {
        if (musicService!!.mediaPlayer != null) {
            playPauseBtn.setIconResource(R.drawable.pause_ic_icon32)
            musicService!!.showNotification(R.drawable.pause_ic_icon32)
            isplaying = true
            musicService!!.mediaPlayer!!.start()
        }
    }

    private fun pauseMusic() {
        if (musicService!!.mediaPlayer != null) {
            playPauseBtn.setIconResource(R.drawable.play_icon32)
            musicService!!.showNotification(R.drawable.play_icon32)
            isplaying = false
            musicService!!.mediaPlayer!!.pause()
        }
    }

    private fun prevNextSong(increment:Boolean){

        if (increment)
        {

            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()

        }else{
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }

    }

    private fun setSongPosition(increment: Boolean){

        if (increment)
        {
            if (musicListPlayer.lastIndex == songPosition){
                songPosition = 0
            }else{
                ++songPosition
            }

        }else{
            if (songPosition == 0){
                songPosition = musicListPlayer.lastIndex
            }else{
                --songPosition
            }
        }

    }


    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService!!.showNotification(R.drawable.pause_32dp)
        musicService!!.seekBarSetup()

    }


    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }

    fun onPlayPauseClick(view: View) {
        isPlaying = !isPlaying

        val drawableRes = if (isPlaying) R.drawable.pause_icon24 else R.drawable.play_icon32
        playPauseBtn.setIconResource(drawableRes)

        if (isPlaying) {
//            val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.image_rotate) // Replace with your actual animation resource ID
//            small_img_layout.startAnimation(rotateAnimation)
            rotateSmallImageLayout()
            playMusic()

        } else {
            small_img_layout.clearAnimation()
            // Stop the rotation animation
            small_img_layout.rotation = 0F
            pauseMusic()
        }
    }

    private fun rotateSmallImageLayout() {

        val rotateAnimation = RotateAnimation(
            0F, 360F,
            RotateAnimation.RELATIVE_TO_SELF, 0.5F,
            RotateAnimation.RELATIVE_TO_SELF, 0.5F
        )
        rotateAnimation.duration = 30000
        // Set the animation to repeat indefinitely
        rotateAnimation.repeatCount = RotateAnimation.INFINITE
        small_img_layout.startAnimation(rotateAnimation)
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    private fun bottomSheet() {
        val layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_playlist, null, false)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(layout)

        val recyclerView = layout.findViewById<RecyclerView>(R.id.drop_up_RecyclerView)
        val title = layout.findViewById<TextView>(R.id.bottom_text_title)
        title?.text = "Playing Queue (${musicListPlayer.size})"

        recyclerView?.layoutManager = LinearLayoutManager(this)
        val adapter = PlayList_Drop(this, musicListPlayer)
        recyclerView?.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                // Swap the items in the list
                Collections.swap(musicListPlayer, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // Remove the item from the list
                musicListPlayer.removeAt(position)
                adapter.notifyItemRemoved(position)
                title?.text = "Playing Queue (${musicListPlayer.size})"
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)
        bottomSheetDialog.show()
    }


    override fun onCompletion(mp: MediaPlayer?) {
        setSongPosition(true)
        createMediaPlayer()
        try {
            setLayout()
        }catch (e:Exception){return}
    }


}


