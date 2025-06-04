package com.example.doggydatingapp.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doggydatingapp.R

class ProfileGalleryAdapter(private val posts: List<String>) : RecyclerView.Adapter<ProfileGalleryAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPost: ImageView = view.findViewById(R.id.iv_post_grid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profile_gallery, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(posts[position])
            .centerCrop()
            .into(holder.ivPost)
    }

    override fun getItemCount(): Int = posts.size
}

