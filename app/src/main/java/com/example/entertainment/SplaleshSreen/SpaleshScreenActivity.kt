package com.example.entertainment.SplaleshSreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import com.example.entertainment.MainActivity
import com.example.entertainment.Permission_Activity_Frist
import com.example.entertainment.R
import com.facebook.shimmer.ShimmerFrameLayout

class SpaleshScreenActivity : AppCompatActivity() {
    private lateinit var shimmer_view_container:ShimmerFrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spaleshsreen)

        shimmer_view_container = findViewById(R.id.shimmer_view_container)
        setTheme(R.style.musicPlayer)
        shimmer_view_container.startShimmer()


        Handler().postDelayed(Runnable {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, Permission_Activity_Frist::class.java)
                startActivity(intent)
            }
            finish()
        }, 2000)

    }
}