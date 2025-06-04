package com.example.doggydatingapp.signin

import android.app.Activity
import com.example.doggydatingapp.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.doggydatingapp.FirebaseInfo
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnCompleteSignIn: MaterialButton
    private lateinit var btnGoogleSignIn: MaterialButton
    private lateinit var btnGoToSignUp: Button

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignIn: GoogleSignIn

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            googleSignIn.handleSignInResult(result.data)
        } else {
            showError("Google Sign-In canceled or failed.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        etEmail = view.findViewById(R.id.et_signin_email)
        etPassword = view.findViewById(R.id.et_signin_password)
        btnCompleteSignIn = view.findViewById(R.id.btn_complete_signin)
        tvErrorMessage = view.findViewById(R.id.tv_error_message)
        btnGoToSignUp = view.findViewById(R.id.btn_go_to_signup)
        btnGoogleSignIn = view.findViewById(R.id.btn_google_signin)

        btnCompleteSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                showError("Please enter both email and password.")
            }
        }

        googleSignIn = GoogleSignIn(
            requireActivity(),
            googleSignInLauncher,
            onSignInSuccess = { account -> handleGoogleSignInSuccess(account.email) },
            onSignInFailure = { error -> showError("Google Sign-In failed: ${error.message}") }
        )

        btnGoogleSignIn.setOnClickListener {
            googleSignIn.startSignIn()
        }

        btnGoToSignUp.setOnClickListener {
            findNavController().navigate(R.id.signup_fragment)
        }

        return view
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.feed_fragment)
                } else {
                    showError("Authentication failed.")
                }
            }
    }

    private fun handleGoogleSignInSuccess(accountEmail: String?) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val email = user.email ?: accountEmail ?: ""
            FirebaseInfo.getUserData(user.uid)
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        findNavController().navigate(R.id.feed_fragment)
                    } else {
                        FirebaseInfo.saveUserBasic(user.uid, email)
                            .addOnSuccessListener { findNavController().navigate(R.id.user_info_fragment) }
                            .addOnFailureListener { showError("Error saving user info.") }
                    }
                }
                .addOnFailureListener { showError("Error checking user info.") }
        } else { showError("Google user is null") }
    }

    private fun showError(message: String) {
        tvErrorMessage.text = message
        tvErrorMessage.visibility = View.VISIBLE
    }
}
