package com.example.doggydatingapp.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doggydatingapp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import github.com.st235.lib_expandablebottombar.ExpandableBottomBar
import github.com.st235.lib_expandablebottombar.navigation.ExpandableBottomBarNavigationUI

class ProfileFragment : Fragment() {

    private lateinit var ivProfileImage: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvTag: TextView
    private lateinit var tvHobbies: TextView
    private lateinit var tvBreed: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ProfileGalleryAdapter
    private val posts = mutableListOf<String>()

    private var userID: String? = null
    private val database = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // in case the fragment is being called by the match pop up
        userID = arguments?.getString("friendId") ?: FirebaseAuth.getInstance().currentUser?.uid

        // initializing layout components
        ivProfileImage = view.findViewById(R.id.iv_profile_image)
        tvUsername = view.findViewById(R.id.tv_username)
        tvTag = view.findViewById(R.id.tv_tag)
        tvHobbies = view.findViewById(R.id.tv_hobbies)
        tvBreed = view.findViewById(R.id.tv_breed)
        recyclerView = view.findViewById(R.id.rv_user_posts)

        adapter = ProfileGalleryAdapter(posts)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        // setting the top tool bar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.top_app_bar)
        toolbar.menu.findItem(R.id.menu_notifications)?.actionView?.findViewById<ImageView>(R.id.iv_notification_icon)
            ?.setOnClickListener {
                findNavController().navigate(R.id.notification_fragment)
            }

        // setting the bottom tool bar
        val bottomBar: ExpandableBottomBar = view.findViewById(R.id.expandable_bottom_bar_profile)
        val navController = findNavController()
        ExpandableBottomBarNavigationUI.setupWithNavController(bottomBar, navController)

        loadUserProfile()
        loadUserPosts()
        return view
    }

    private fun loadUserProfile() {
        val userID = userID ?: return
        val userDoc = database.collection("users").document(userID)

        userDoc.get().addOnSuccessListener { document ->
            if (document != null)  {
                // setting the variables at the top of the profile frag.
                tvUsername.text = document.getString("username")
                tvTag.text = document.getString("tag") ?: "Single"
                tvHobbies.text = document.getString("hobbies")
                tvBreed.text = document.getString("breed")

                // setting profile image
                Glide.with(requireContext())
                    .load(document.getString("profileImageUrl"))
                    .into(ivProfileImage)
            }
        }
    }

    private fun loadUserPosts() {
        val userID = userID ?: return
        val userDoc = database.collection("posts").whereEqualTo("userId", userID)

        userDoc.get().addOnSuccessListener { document ->
            if (document != null)  {
                posts.clear() // removing everything from the array of post urls
                for (post in document) {
                    val imageUrl = post.getString("postImageUrl")
                    if (!imageUrl.isNullOrEmpty()) posts.add(imageUrl)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}
