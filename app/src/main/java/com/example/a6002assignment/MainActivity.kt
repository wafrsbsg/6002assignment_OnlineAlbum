package com.example.a6002assignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.a6002assignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToCamera(view: View){
        val getIntent = intent
        val textView = findViewById<TextView>(R.id.texttext)
        val uid = intent.getStringExtra("uid")
        textView.text = uid

        val intent= Intent(this,CameraActivity::class.java)
        intent.putExtra("uid", uid);

        startActivity(intent)
    }
}