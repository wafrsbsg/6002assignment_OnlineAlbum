package com.example.a6002assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MicActivity : AppCompatActivity() {
    private val REQUEST_CODE_MIC = 1
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.getReferenceFromUrl("gs://assignment-d3570.appspot.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mic)
        val micButton = findViewById<Button>(R.id.btn_mic)

        micButton.setOnClickListener {
                //Call the speech recognizer when the micButton is clicked
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                startActivityForResult(intent, REQUEST_CODE_MIC)
        }
    }

    //Display speech recognition results and upload the results to realtime database
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val resultView = findViewById<TextView>(R.id.tv_result)
        //Get user ID and unique key from CameraActivity
        val getIntent = intent
        val uid = intent.getStringExtra("uid")
        val key = intent.getStringExtra("key")

        super.onActivityResult(requestCode, resultCode, data)
        //checking request code with result code
        if (requestCode == REQUEST_CODE_MIC) {
            //Check if data is null
            if (data != null) {
                //Extract data from array list
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                //Display the result in resultView
                resultView.text = result[0]

                val uploadButton2: Button = findViewById(R.id.btn_upload2)
                uploadButton2.setOnClickListener {
                    //Use the setValue method to write result to the specific location of the firebase realtime database,
                    //uid and key Get from CameraActivity
                    var database: DatabaseReference = Firebase.database.reference
                    database.child("user").child(uid.toString()).child(key.toString())
                        .child("comment").setValue(result[0])
                    Toast.makeText(applicationContext, "Upload Succeeded", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

