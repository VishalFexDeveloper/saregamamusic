package com.example.entertainment

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.entertainment.MainActivty.SecondActivityVideoFloder
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.ModalData.VideoDataMadal
import com.example.entertainment.databinding.BoosterLayoutBinding
import com.example.entertainment.databinding.MoreFeaturesBinding
import com.example.entertainment.databinding.SpeedDialogLayoutBinding
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.DecimalFormat
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.system.exitProcess

class VideoPlayerActivity : AppCompatActivity() {


    private lateinit var playerActivity: PlayerView
    private lateinit var videotitle:TextView
    private lateinit var backBtn:ImageButton
    private lateinit var playerPauseBtn: ImageButton
    private lateinit var prevBtn:ImageButton
    private lateinit var nextBtn:ImageButton
    private lateinit var topLayout: LinearLayout
    private lateinit var bottomLayout: RelativeLayout
    private lateinit var lockScreenBtn:ImageButton
    private lateinit var repeatBtn:ImageButton
    private lateinit var fullScreenBtn:ImageButton
    private lateinit var moreFeaturesBtn: ImageButton
    private var isSubtitle:Boolean = true


    private lateinit var runnable: Runnable

    companion object {
        private var timer:Timer? = null
        lateinit var player: SimpleExoPlayer
        lateinit var playerList: ArrayList<VideoDataMadal>
        var position = -1
        var repeart:Boolean = false
        private var isFullScreen:Boolean = false
        private var isLocked:Boolean = false
        @SuppressLint("StaticFieldLeak")
        private lateinit var trackSelector: DefaultTrackSelector
        private lateinit var loudnessEnhancer: LoudnessEnhancer
        private var speed: Float = 1.0f
        var pipStatus: Int = 0
    }

    @SuppressLint("MissingInflatedId", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_video_player)


        setTheme(R.style.playerActivityTheme)

        playerActivity = findViewById(R.id.player_video)
        videotitle = findViewById(R.id.video_title_Marquee)
        backBtn = findViewById(R.id.backBtn_player)
        playerPauseBtn = findViewById(R.id.playPauseBtn)
        prevBtn = findViewById(R.id.prevBtn)
        nextBtn = findViewById(R.id.nextBtn)
        topLayout = findViewById(R.id.topController)
        repeatBtn = findViewById(R.id.repeatBtn)
        fullScreenBtn = findViewById(R.id.fullscreenBtn)
        bottomLayout = findViewById(R.id.bottomController)
        lockScreenBtn = findViewById(R.id.screenLockBtn)
        moreFeaturesBtn = findViewById(R.id.moreFeaturesBtn)


        clickRun()


        initializeLayout()

        backBtn.setOnClickListener {
            Toast.makeText(this, "Back Activity", Toast.LENGTH_SHORT).show()
            finish()
        }

        playerPauseBtn.setOnClickListener {
            if (player.isPlaying){
                pauseDestroy()
            }else{
                playDestroy()
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }




    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    private fun clickRun(){

        nextBtn.setOnClickListener {
            nextPrevVideo(isNext = true)
        }
        prevBtn.setOnClickListener {
            nextPrevVideo(isNext = false)
        }
        repeatBtn.setOnClickListener {
            if (repeart){
                repeart = false
                Toast.makeText(this, "Repeat All Video", Toast.LENGTH_SHORT).show()
                player.repeatMode = Player.REPEAT_MODE_OFF
                repeatBtn.setImageResource(R.drawable.repeat_fill_icon_24)
            }else{
                repeart = true
                Toast.makeText(this, "Repeat One Video", Toast.LENGTH_SHORT).show()
                player.repeatMode = Player.REPEAT_MODE_ONE
                repeatBtn.setImageResource(R.drawable.repeat_option_off24)
            }
        }
        fullScreenBtn.setOnClickListener {
            if (isFullScreen){
                isFullScreen = false
                playInFullScreen(enable = false)
            }else{
                isFullScreen = true
                playInFullScreen(enable = true)
            }
        }

        lockScreenBtn.setOnClickListener {
            if (!isLocked){

                isLocked = true
                Toast.makeText(this, "Screen Lock", Toast.LENGTH_SHORT).show()
                playerActivity.hideController()
                playerActivity.useController = false
                lockScreenBtn.setImageResource(R.drawable.lock_bassic_device_icon_24)
            }else{
                isLocked = false
                Toast.makeText(this, "Screen Unlock", Toast.LENGTH_SHORT).show()
                playerActivity.useController = true
                playerActivity.showController()
                lockScreenBtn.setImageResource(R.drawable.device_unlock_icon_24)
            }
        }

        moreFeaturesBtn.setOnClickListener {
            pauseDestroy()

            Toast.makeText(this, "More Features", Toast.LENGTH_SHORT).show()

            val moreFeaturesLayout = LayoutInflater.from(this).inflate(R.layout.more_features,null,false)
            val bindingMf = MoreFeaturesBinding.bind(moreFeaturesLayout)
           val dialog = MaterialAlertDialogBuilder(this).setView(moreFeaturesLayout)
               .setOnCancelListener { playDestroy() }
               .setBackground(ColorDrawable(0x80F4511E.toInt()))
               .create()
            dialog.show()
            bindingMf.audioTrack.setOnClickListener {
                dialog.dismiss()
                pauseDestroy()
                val audioTrack = ArrayList<String>()

                for(i in 0 until player.currentTrackGroups.length){
                    if (player.currentTrackGroups.get(i).getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT){
                        audioTrack.add(Locale(player.currentTrackGroups.get(i).getFormat(0).language.toString()).displayLanguage)

                    }else{
                        audioTrack.add("Unknown Language")
                    }
                }
                val temTracks = audioTrack.toArray(arrayOfNulls<CharSequence>(audioTrack.size))

                MaterialAlertDialogBuilder(this,R.style.alertDialog)
                    .setTitle("Select Language")
                    .setOnCancelListener { playDestroy() }
                    .setBackground(ColorDrawable(0x80F4511E.toInt()))
                    .setItems(temTracks){_, position ->
                        Toast.makeText(this,audioTrack[position] + " Selected" , Toast.LENGTH_SHORT).show()
                        playDestroy()
                        trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage(audioTrack[position]))

                    }
                    .show()



            }
            bindingMf.subTitlesBtn.setOnClickListener {
                if (isSubtitle){
                    trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(this).setRendererDisabled(
                        C.TRACK_TYPE_VIDEO, true
                    ).build()
                    Toast.makeText(this," Subtitles Off" , Toast.LENGTH_SHORT).show()
                    isSubtitle = false
                }else{
                    trackSelector.parameters = DefaultTrackSelector.ParametersBuilder(this).setRendererDisabled(
                        C.TRACK_TYPE_VIDEO, false
                    ).build()
                    Toast.makeText(this,"Subtitles On" , Toast.LENGTH_SHORT).show()
                    isSubtitle = true
                }
                dialog.dismiss()
                playDestroy()
            }

            bindingMf.audioBooster.setOnClickListener {
                dialog.dismiss()

                Toast.makeText(this, "Booster on", Toast.LENGTH_SHORT).show()

                val moreFeaturesLayoutB = LayoutInflater.from(this).inflate(R.layout.booster_layout, null, false)
                val bindingB = BoosterLayoutBinding.bind(moreFeaturesLayoutB)
                val dialogB = MaterialAlertDialogBuilder(this).setView(moreFeaturesLayoutB)
                    .setOnCancelListener { playDestroy() }
                    .setPositiveButton("Ok") { self, _ ->
                        val targetGain = bindingB.seekBar.progress * 100
                        loudnessEnhancer.setTargetGain(targetGain)
                        playDestroy()
                        self.dismiss()
                    }
                    .setBackground(ColorDrawable(0x80F4511E.toInt()))
                    .create()

                dialogB.show()

                bindingB.seekBar.progress = loudnessEnhancer.targetGain.toInt() / 100

                var mMin = 0
                var mMax = 15
                var mCurrent = 10

                bindingB.seekBar.min = mMin
                bindingB.seekBar.max = mMax

                bindingB.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        mCurrent = progress
                        bindingB.seekBarText.text = mCurrent.toString()

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
                playDestroy()
            }
            bindingMf.speedBtn.setOnClickListener {

                dialog.dismiss()
                playDestroy()
                Toast.makeText(this, "Booster on", Toast.LENGTH_SHORT).show()

                val moreFeaturesLayoutS = LayoutInflater.from(this).inflate(R.layout.speed_dialog_layout, null, false)
                val bindingS = SpeedDialogLayoutBinding.bind(moreFeaturesLayoutS)
                val dialogS = MaterialAlertDialogBuilder(this).setView(moreFeaturesLayoutS)
                    .setCancelable(false)
                    .setPositiveButton("Ok") { self, _ ->

                        self.dismiss()
                    }

                    .setBackground(ColorDrawable(0x80F4511E.toInt()))
                    .create()

                val decimalFormat = DecimalFormat("#,##.#")

                val updateSpeedText = {
                    val formattedSpeed = "${decimalFormat.format(speed)} X"
                    bindingS.speedText.text = formattedSpeed
                }

                bindingS.minusBtn.setOnClickListener {
                    changeSpeed(isIncrement = false)
                    updateSpeedText()
                }

                bindingS.plusBtn.setOnClickListener {
                    changeSpeed(isIncrement = true)
                    updateSpeedText()
                }

// Initial setup
                updateSpeedText()

                dialogS.show()



            }

            bindingMf.sleepTimer.setOnClickListener {

                dialog.dismiss()

                if (timer != null){
                    Toast.makeText(this, "Timer already Running!!\nClose App to Reset Timer!!", Toast.LENGTH_SHORT).show()
                }else{
                    var sleepTime  = 15
                    Toast.makeText(this, "Booster on", Toast.LENGTH_SHORT).show()

                    val moreFeaturesLayoutS = LayoutInflater.from(this).inflate(R.layout.speed_dialog_layout, null, false)
                    val bindingS = SpeedDialogLayoutBinding.bind(moreFeaturesLayoutS)
                    val dialogS = MaterialAlertDialogBuilder(this).setView(moreFeaturesLayoutS)
                        .setCancelable(false)
                        .setPositiveButton("Ok") { self, _ ->
                            timer = Timer()
                            val task = object:TimerTask(){

                                override fun run() {
                                    moveTaskToBack(true)
                                    exitProcess(1)
                                }

                            }
                            timer!!.schedule(task,sleepTime*60*1000.toLong())
                            self.dismiss()
                            playDestroy()
                        }
                        .setBackground(ColorDrawable(0x80F4511E.toInt()))
                        .create()


                    bindingS.speedText.text = "$sleepTime Min"

                    bindingS.minusBtn.setOnClickListener {
                        if (sleepTime > 15){
                            sleepTime -= 15
                        }
                        bindingS.speedText.text = "$sleepTime Min"
                    }

                    bindingS.plusBtn.setOnClickListener {
                        if (sleepTime < 120){
                            sleepTime += 15
                        }
                        bindingS.speedText.text = "$sleepTime Min"
                    }

                    dialogS.show()

                }


            }

            bindingMf.pipModeBtn.setOnClickListener {

                val appOps = getSystemService(APP_OPS_SERVICE) as AppOpsManager
                val isPiPSupported = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    appOps.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, Process.myUid(), packageName) == AppOpsManager.MODE_ALLOWED
                } else {
                    false
                }

                if (isPiPSupported) {
                    this.enterPictureInPictureMode(PictureInPictureParams.Builder().build())
                    playDestroy()
                    dialog.dismiss()
                    playerActivity.hideController()
                    pipStatus = 0

                } else {
                    val intent = Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.parse("package:$packageName"))
                    startActivity(intent)
                }
            }


        }



    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        if (isInPictureInPictureMode) {
            playerActivity.hideController()
            playDestroy()
        } else {
            // PiP mode is exited, show the video controller
            playerActivity.showController()
        }
        if (pipStatus != 0){
            finish()
            val intent = Intent(this, VideoPlayerActivity::class.java)
            when(pipStatus){
                1-> intent.putExtra("class","FolderActivity")

                2-> intent.putExtra("class","AllVideos")
            }

            startActivity(intent)
        }

    }






    private fun initializeLayout() {

        when (intent.getStringExtra("class")) {
            "AllVideos" -> {
                playerList = ArrayList()
                playerList.addAll(MusicFragmentParent.videoList)
                player = SimpleExoPlayer.Builder(this).build()  // Initialize player
                createPlayer()
            }

            "FolderActivity" -> {
                playerList = ArrayList()
                playerList.addAll(SecondActivityVideoFloder.currentFolderVideos)
                player = SimpleExoPlayer.Builder(this).build()  // Initialize player
                createPlayer()
            }

        }
        if (repeart){
            repeatBtn.setImageResource(R.drawable.repeat_option_off24)
        }else{
            repeatBtn.setImageResource(R.drawable.repeat_fill_icon_24)
        }

    }



    private fun playDestroy(){

        playerPauseBtn.setImageResource(R.drawable.pause_ic_icon32)
        player.play()
    }

    private fun pauseDestroy(){

        playerPauseBtn.setImageResource(R.drawable.play_icon32)
        player.pause()
    }

    private fun createPlayer() {
        try { player.release() }catch (_:Exception){}
        speed = 1.0f
        trackSelector = DefaultTrackSelector(this)

        player = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        playerActivity.player = player
        val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(playerList[position].artUri)
        videotitle.text = playerList[position].title
        videotitle.isSelected = true
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        playDestroy()
        player.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                if (playbackState == Player.STATE_ENDED){
                    nextPrevVideo()
                }

            }
        })
        playInFullScreen(enable = isFullScreen)
        setVisibility()
        loudnessEnhancer = LoudnessEnhancer(player.audioSessionId)
        loudnessEnhancer.enabled = true


    }


    private fun nextPrevVideo(isNext:Boolean = true){
        player.release()

       if (isNext){
           setPosition()

       }else{
           setPosition(isIncrement = false)
       }
        createPlayer()

    }

    private fun setPosition(isIncrement:Boolean = true){

        if (!repeart){
            if (isIncrement){
                if (playerList.lastIndex == position){
                    position = 0
                }else{
                    ++position
                }
            }else{
                if (position  == 0){
                    position = playerList.lastIndex
                }else{
                    --position
                }
            }

        }

    }

    private fun playInFullScreen(enable:Boolean){
        if (enable){
            playerActivity.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            fullScreenBtn.setImageResource(R.drawable.full_screen_exit_small)
        }else{
            playerActivity.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            fullScreenBtn.setImageResource(R.drawable.full_screen_button_sl)
        }

    }


    private fun setVisibility(){
        runnable = Runnable {
            if (playerActivity.isControllerVisible){
                changeVisibility(View.VISIBLE)
            }else{
                changeVisibility(View.GONE)
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable, 50)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)
    }

    private fun changeVisibility(visibility: Int){
        topLayout.visibility = visibility
        bottomLayout.visibility = visibility
        lockScreenBtn.visibility = visibility
        fullScreenBtn.visibility = visibility
        playerPauseBtn.visibility = visibility


        if (isLocked){
            lockScreenBtn.visibility = View.VISIBLE
        }else{
            lockScreenBtn.visibility = visibility
        }

    }

    private fun changeSpeed(isIncrement: Boolean){
        if (isIncrement){
            if (speed <= 2.9f){
                speed+= 0.10f
            }
        }else{
            if (speed > 0.20f ){
                speed -= 0.10f
            }
        }
        player.setPlaybackSpeed(speed)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        Toast.makeText(this, "Back Activity", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onPause() {
        super.onPause()
        pauseDestroy()
    }

}
