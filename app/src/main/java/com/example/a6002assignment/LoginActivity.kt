package com.example.a6002assignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var loginAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginAuth = Firebase.auth
        title = "Login"
    }

    fun login(view: View) {
        val loginEmailText = findViewById<TextView>(R.id.text_loginEmail)
        val loginPasswordText = findViewById<TextView>(R.id.text_loginPassword)
        val loginEmail = loginEmailText.text.toString()
        val loginPassword = loginPasswordText.text.toString()

            loginAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //get user id
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("uid",
                            FirebaseAuth.getInstance().currentUser!!.uid.toString())
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG).show()
                }
        }



    fun goToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}

