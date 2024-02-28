package com.example.entertainment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import com.example.entertainment.MainFragment.MusicFragmentParent

class Permission_Activity_Frist : AppCompatActivity() {
    private lateinit var startBtn:AppCompatButton
    private lateinit var service_text:TextView
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_frist)

        startBtn = findViewById(R.id.start_Btn_per)
        service_text = findViewById(R.id.service_text)


        val htmlString = getString(R.string.by_tapping)
        service_text.text = Html.fromHtml(htmlString)

        // Enable links to be clickable
        service_text.movementMethod = LinkMovementMethod.getInstance()

        // Set the link text color programmatically (in this case, green)
        service_text.setLinkTextColor(resources.getColor(R.color.green))

        startBtn.setOnClickListener {
            if (checkPermission()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        
    }



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermission(): Boolean {
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
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permissions Allow", Toast.LENGTH_SHORT).show()
            return true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.FOREGROUND_SERVICE
                ),
                12
            )
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 12 && grantResults.isNotEmpty()) {


            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_AUDIO
                ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ) {

                // Permissions are granted
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            } else {
                val showRationale = permissions.any {
                    ActivityCompat.shouldShowRequestPermissionRationale(this, it)
                }

                if (!showRationale) {
                    // User selected "Never ask again", show a dialog or redirect to settings
                    showPermissionSettingsDialog()
                } else {
                    // User denied the permission, you can show a rationale and request again
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.FOREGROUND_SERVICE
                        ),
                        12
                    )
                }
            }
        }
    }

    private fun showPermissionSettingsDialog() {
        // Show a dialog or redirect the user to the app settings
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }
}