package com.example.entertainment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.entertainment.MainFragment.MusicFragmentParent
import com.example.entertainment.MainFragment.VideoFragmentParent
import com.example.entertainment.MainFragment.YouFragmentParent
import com.example.entertainment.ModalData.AlbumsData
import com.example.entertainment.ModalData.FolderData
import com.example.entertainment.ModalData.FolderVideo
import com.example.entertainment.ModalData.Music
import com.example.entertainment.ModalData.VideoDataMadal
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dark mood application
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        bottomNavigationView = findViewById(R.id.bottom_nav_BottomNavigationView)


        //add fragment MusicFragmentParent only first fragment
        addFragment(MusicFragmentParent<Any>())

        ///bottom bottomNavigationView set fragment
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Music -> {
                    addFragment(MusicFragmentParent<Any>())
                }

                R.id.Video -> {
                    addFragment(VideoFragmentParent())
                }

                R.id.You -> {
                    addFragment(YouFragmentParent())
                }
            }
            true
        }

         
    }



    // create fun addFragment
    private fun addFragment(fragment: Fragment) {
        val layout = supportFragmentManager.beginTransaction()
        layout.replace(R.id.frameLayout, fragment)
        layout.commit()

    }















}