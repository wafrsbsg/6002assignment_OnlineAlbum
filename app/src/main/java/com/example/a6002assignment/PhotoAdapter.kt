package com.example.a6002assignment

import Users
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList

class PhotoAdapter(private val users: ArrayList<Users>, private val context: Context) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pic1 : ImageView
        val comment1 : TextView
        init {
            pic1 = view.findViewById(R.id.iv_viewPhoto)
            comment1 = view.findViewById(R.id.tv_comment)
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
        Glide.with(context).load(users[position].pic).into(holder.pic1)
        holder.comment1.text = users[position].comment

        //When users click itemView, they will send users' element
        // to PhotoDetailActivity and go to PhotoDetailActivity.
        // At that time, users can see the details of the clicked photos
        holder.itemView.setOnClickListener {
            val pic = users[position].pic
            val latitude = users[position].latitude
            val longitude = users[position].longitude
            val intent = Intent(context,PhotoDetailActivity::class.java)
            intent.putExtra("pic",pic)
            intent.putExtra("latitude",latitude)
            intent.putExtra("longitude",longitude)
            context.startActivity(intent)
        }
    }

    //Return the size of Users
    override fun getItemCount() = users.size
}