package com.example.firebasetesting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserData: User

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        currentUserData = intent.getSerializableExtra("currentUserData") as User

        println("Name: ${currentUserData.name}, email: ${currentUserData.email}")

        val txtWelcome: TextView = findViewById(R.id.txt_welcome)
        val txtEmail: TextView = findViewById(R.id.txt_show_email)
        val btnSignOut: Button = findViewById(R.id.btn_sign_out)

        txtWelcome.text = txtWelcome.text.toString() + currentUserData.name
        txtEmail.text = txtEmail.text.toString() + currentUserData.email

        btnSignOut.setOnClickListener {
            auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}