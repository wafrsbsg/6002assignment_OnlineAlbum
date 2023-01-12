package com.example.a6002assignment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class CameraActivity : AppCompatActivity() {
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.getReferenceFromUrl("gs://assignment-d3570.appspot.com")
    private val REQUEST_CODE_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        val photoButton: Button = findViewById(R.id.btn_photo)
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        //check permission
        photoButton.setOnClickListener {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
            }
        }
        title = "Take photos and upload"

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Get user ID from Firebase Authentication
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        var photoView: ImageView = findViewById(R.id.iv_photo)

        super.onActivityResult(requestCode, resultCode, data)
        //checking request code with result code
        if (requestCode == REQUEST_CODE_CAMERA) {
            //Get data of photo and display
            val photo = data?.extras?.get("data") as Bitmap
            photoView.setImageBitmap(photo)
            //Create a reference to a photo for firebase storage
            val reference = storageRef.child(photo.toString())

            //Get the data from an photoView as bytes
            photoView.isDrawingCacheEnabled = true
            photoView.buildDrawingCache()
            val bitmap = photoView.drawingCache
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            //upload photo
            val uploadButton: Button = findViewById(R.id.btn_upload)
            uploadButton.setOnClickListener {
                //When the uploadButton is clicked, the file will be uploaded to Firebase Cloud Storage using the putBytes() method
                val uploadTask = reference.putBytes(data)
                uploadTask.addOnFailureListener {
                    //When the upload fails, toast will display
                    Toast.makeText(applicationContext, "Upload Failed", Toast.LENGTH_LONG).show()
                }
                    .addOnSuccessListener { taskSnapshot ->
                        //When the upload is successful, use the getDownloadUrl() method to obtain the url of the uploaded photo
                        val task: Task<Uri> = taskSnapshot.metadata?.reference!!.downloadUrl
                        task.addOnSuccessListener { uri ->
                            //The variable photoUri is equal to the url of the uploaded photo
                            val photoUri = uri.toString()

                            //Use the push method to write photoUri to the specific location of the firebase realtime database,
                            //uid Get from Firebase Authentication
                            var database: DatabaseReference = Firebase.database.reference
                            val databseReference = database.child("user").child(uid).push()
                            databseReference.child("pic").setValue(photoUri)

                            //Get the unique key of the uploaded image from the realtime database
                            var key = databseReference.key
                            Toast.makeText(applicationContext, "Upload succeeded", Toast.LENGTH_LONG).show()

                            //Pass the key obtained from realtime database to MicGpsActivity
                            val intent = Intent(this, MicGpsActivity::class.java)
                            intent.putExtra("key", key)

                            startActivity(intent)


                        }
                    }
            }
        }

    }
}


