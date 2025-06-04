package com.example.doggydatingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

class OnboardingFragment : Fragment() {

    private lateinit var btnGoToSignIn: ImageButton
    private lateinit var gifView1: ImageView
    private lateinit var gifView2: ImageView
    private lateinit var gifView3: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)
        gifView1 = view.findViewById(R.id.gifView)
        gifView2 = view.findViewById(R.id.gifView2)
        gifView3 = view.findViewById(R.id.gifView3)
        btnGoToSignIn = view.findViewById(R.id.btn_to_signin)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_two)
            .into(gifView1)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_three)
            .into(gifView2)

        Glide.with(this)
            .asGif()
            .load(R.drawable.gif_four)
            .into(gifView3)

        btnGoToSignIn.setOnClickListener {
            findNavController().navigate(R.id.signin_fragment)
        }
        return view
    }
}