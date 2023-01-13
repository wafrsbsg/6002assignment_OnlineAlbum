package com.example.a6002assignment

import Users
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class PhotoAdapter(private val users: ArrayList<Users>, private val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val viewPhoto: ImageView
        val viewComment: TextView
        val viewLocation: TextView
        val buttonDelete: Button

        init {
            viewPhoto = view.findViewById(R.id.iv_viewPhoto)
            viewComment = view.findViewById(R.id.tv_comment)
            viewLocation = view.findViewById(R.id.tv_location)
            buttonDelete = view.findViewById(R.id.btn_delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Create new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Get the contents of the element replacement view from Users
        //Use Glide to load photo
        Glide.with(context).load(users[position].pic).into(holder.viewPhoto)
        holder.viewComment.text = users[position].comment
        holder.viewLocation.text = users[position].address

        //When users click itemView, they will send users' element
        // to PhotoDetailActivity and go to PhotoDetailActivity.
        // At that time, users can see the details of the clicked photos
        holder.itemView.setOnClickListener {
            //Get the contents of the element from Users
            val pic = users[position].pic
            val latitude = users[position].latitude
            val longitude = users[position].longitude
            val comment = users[position].comment
            val address = users[position].address

            val intent = Intent(context,PhotoDetailActivity::class.java)
            intent.putExtra("pic",pic)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            intent.putExtra("comment",comment)
            intent.putExtra("address",address)
            context.startActivity(intent)
        }

        //When the user clicks the delete button on the list,
        // the specified picture and its data can be deleted
        holder.buttonDelete.setOnClickListener {
            //Get the contents of the element from Users
            val key = users[position].key
            //Get user ID from Firebase Authentication
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            //Get an instance of the specified location
            var database: DatabaseReference = FirebaseDatabase.getInstance().reference
            var databaseReference = database.child("user").child(uid)
            //Use the removeValue() method of realtime database to delete the child at the specified location
            //After storing its own unique key, find the specified unique key through the stored key and delete it
            databaseReference.child(key.toString()).removeValue()
            val intent = Intent(context, PhotoActivity::class.java)
            //Finish and restart activity
            (context as Activity).finish()
            (context).overridePendingTransition(0, 0)
            context.startActivity(intent)
            (context).overridePendingTransition(0, 0)
        }
    }

    //Return the size of Users
    override fun getItemCount() = users.size
}