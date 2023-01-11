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
        val testimg = findViewById<ImageView>(R.id.testimg)
        title = "Main"
       // testimg.setImageBitmap(getBitmapFromURL("https://firebasestorage.googleapis.com/v0/b/assignment-d3570.appspot.com/o/android.graphics.Bitmap%40b4c352f?alt=media&token=d6548bd7-34e8-4dab-91d5-e8ea76c927b8"))
        }






    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }



        fun test(view: View) {
            val textLocation = findViewById<TextView>(R.id.texttext)
            val float = FloatArray(1)
            Location.distanceBetween(37.421998333333335, -122.084, 37.2638, -122.1017, float)


            Toast.makeText(applicationContext, float[0].toString(), Toast.LENGTH_LONG).show()
        }


        fun goToCamera(view: View) {
            val getIntent = intent
            val textView = findViewById<TextView>(R.id.texttext)
            val uid = intent.getStringExtra("uid")
            textView.text = uid

            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("uid", uid);

            startActivity(intent)
        }

        fun goToPicture(view: View) {
            val getIntent = intent
            val textView = findViewById<TextView>(R.id.texttext)
            val uid = intent.getStringExtra("uid")
            textView.text = uid

            val intent = Intent(this, PhotoActivity::class.java)
            intent.putExtra("uid", uid);

            startActivity(intent)
        }


}