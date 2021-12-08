package com.example.firebasetesting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Model user area to demonstrate getting user data from the database, and taking a custom class
 * passed via Intent
 */
class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserData: User
    private lateinit var txtErrors: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        currentUserData = intent.getSerializableExtra("currentUserData") as User

        txtErrors = findViewById(R.id.txt_errors)
        txtErrors.text = ""

        val txtWelcome: TextView = findViewById(R.id.txt_welcome)
        val txtEmail: TextView = findViewById(R.id.txt_show_email)
        val btnSignOut: Button = findViewById(R.id.btn_sign_out)
        val btnUserArea: Button = findViewById(R.id.btn_user_area)
        val btnAdminArea: Button = findViewById(R.id.btn_admin_area)

        txtWelcome.text = txtWelcome.text.toString() + " ${currentUserData.name}"
        txtEmail.text = txtEmail.text.toString() + " ${currentUserData.email}"

        btnSignOut.setOnClickListener {
            auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnUserArea.setOnClickListener {
            if(AccessCheckers.isUser(currentUserData)){
                txtErrors.text = ""
                Toast.makeText(this, "USER AREA DISPLAYED", Toast.LENGTH_LONG).show()
            } else {
                showAccessDenied()
            }
        }

        btnAdminArea.setOnClickListener {
            if(AccessCheckers.isAdmin(currentUserData)) {
                txtErrors.text = ""
                Toast.makeText(this, "ADMIN AREA DISPLAYED", Toast.LENGTH_LONG).show()
            } else {
                showAccessDenied()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showAccessDenied() {
        txtErrors.text = "Access Denied. Current role: " + currentUserData.role
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}