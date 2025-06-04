package com.example.doggydatingapp.signup

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.doggydatingapp.R
import com.example.doggydatingapp.FirebaseInfo
import com.example.doggydatingapp.signin.GoogleSignIn
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnCompleteSignUp: MaterialButton
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnGoogleSignUp: MaterialButton
    private lateinit var googleSignUp: GoogleSignIn

    private val googleSignUpLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            googleSignUp.handleSignInResult(result.data)
        } else {
            showError("Google Sign-Up canceled.")
        }
    }

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        etEmail = view.findViewById(R.id.et_signup_email)
        etPassword = view.findViewById(R.id.et_signup_password)
        btnCompleteSignUp = view.findViewById(R.id.btn_complete_signup)
        tvErrorMessage = view.findViewById(R.id.tv_error_message)
        btnGoogleSignUp = view.findViewById(R.id.btn_google_signup)

        btnCompleteSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signUp(email, password)
            } else {
                showError("Please enter the name, email and password.")
            }
        }

        googleSignUp = GoogleSignIn(
            requireActivity(),
            googleSignUpLauncher,
            onSignInSuccess = { account ->
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    val email = user.email ?: account.email ?: ""

                    FirebaseInfo.getUserData(user.uid)
                        .addOnSuccessListener {
                            if (it.exists()) {
                                findNavController().navigate(R.id.feed_fragment)
                            } else {
                                FirebaseInfo.saveUserBasic(user.uid, email)
                                    .addOnSuccessListener { findNavController().navigate(R.id.user_info_fragment) }
                                    .addOnFailureListener { showError("Error saving user info.") }
                            }
                        }
                        .addOnFailureListener { showError("Error checking user info.") }
                } else {
                    showError("Google user is null")
                }
            },
            onSignInFailure = { error -> showError("Google Sign-In failed: ${error.message}") }
        )

        btnGoogleSignUp.setOnClickListener {
            googleSignUp.startSignIn()
        }

        return view
    }

    private fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        FirebaseInfo.saveUserBasic(it.uid, email)
                            .addOnSuccessListener { Log.d("SignUpFragment", "User profile created in Firestore") }
                            .addOnFailureListener { e -> Log.e("SignUpFragment", "Error creating user profile", e) }
                    }
                    findNavController().navigate(R.id.user_info_fragment)
                } else {
                    showError("Sign Up failed.")
                }
            }
    }

    private fun showError(message: String) {
        tvErrorMessage.text = message
        tvErrorMessage.visibility = View.VISIBLE
    }
}
