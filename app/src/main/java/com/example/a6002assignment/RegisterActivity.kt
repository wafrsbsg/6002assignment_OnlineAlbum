package com.example.a6002assignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerAuth = Firebase.auth
        title = "Register"
    }

    fun register(view: View){
        val registerEmailText = findViewById<TextView>(R.id.text_registerEmail)
        val registerPasswordText = findViewById<TextView>(R.id.text_registerPassword)
        val registerEmail = registerEmailText.text.toString()
        val registerPassword = registerPasswordText.text.toString()

        registerAuth.createUserWithEmailAndPassword(registerEmail,registerPassword).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                val user = Firebase.auth.currentUser
                user?.let {
                    val uid = user.uid
                    //data class User(val pic: String? = null) {
                    // Null default values create a no-argument default constructor, which is needed
                    // for deserialization from a DataSnapshot.
                    //}
                    //val database = FirebaseDatabase.getInstance()
                    var database: DatabaseReference = Firebase.database.reference
                    val userUid : String? = null
                    //val refUid = database.getReference("uid")
                    //refUid.setValue(uid)
                    //val userDbRef = database.child("user").child(uid.toString())
                    //userDbRef.push()
                    val intent= Intent(this,MainActivity::class.java)
                    intent.putExtra("uid", uid)
                    startActivity(intent)
                    finish()
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}