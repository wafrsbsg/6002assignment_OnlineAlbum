package com.example.a6002assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        title = "Online Album"
    }

    fun goToLoginM(view: View) {
        //go to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun goToRegisterM(view: View) {
        //go to LoginActivity
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}