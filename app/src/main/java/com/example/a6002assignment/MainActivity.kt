package com.example.a6002assignment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Home"
        }

        fun goToCamera(view: View) {
            //When the button is clicked, the user ID will be get from LoginActivity or RegisterActivity and sent to CameraActivity
            val uid = intent.getStringExtra("uid")
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("uid", uid);
            startActivity(intent)
        }

        fun goToPicture(view: View) {
            //When the button is clicked, the user ID will be get from LoginActivity or RegisterActivity and sent to PhotoActivity
            val uid = intent.getStringExtra("uid")
            val intent = Intent(this, PhotoActivity::class.java)
            intent.putExtra("uid", uid);
            startActivity(intent)
        }


}