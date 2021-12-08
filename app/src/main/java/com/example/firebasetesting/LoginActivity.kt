package com.example.firebasetesting

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

/**
 * Class to handle the login page logic. Can redirect to the registration page, or to the user
 * area if already logged in.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRealtime: FirebaseDatabase
    private lateinit var dbOperations: DatabaseOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // getting firebase connections
        auth = Firebase.auth

        // TODO: your DB URL here
        dbRealtime = FirebaseDatabase.getInstance()

        val editEmail: EditText = findViewById(R.id.editLoginEmail)
        val editPassword: EditText = findViewById(R.id.editLoginPassword)
        val btnSubmit: Button = findViewById(R.id.btnLogin)
        val btnGoToRegister: Button = findViewById(R.id.btnGoToRegister)

        btnSubmit.setOnClickListener {
            auth.signInWithEmailAndPassword(
                editEmail.text.toString(),
                editPassword.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    Toast.makeText(
                        this, "Logged in with ${auth.currentUser?.email}",
                        Toast.LENGTH_LONG
                    ).show()

                    println(auth.currentUser!!.uid)
                    loadLoggedInUI(auth.currentUser!!.uid)

                } else {
                    Log.d("TAG", "signInWithEmail:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            this, "Incorrect email or password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        if (currentUser != null) {
            Toast.makeText(this, "Already logged in!", Toast.LENGTH_LONG).show()
            println(auth.currentUser!!.uid)
            loadLoggedInUI(auth.currentUser!!.uid)
        }
    }

    private fun loadLoggedInUI(userID: String) {
        val dbReference = dbRealtime.getReference("Users")
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = User()

                user.name = snapshot.child("${userID}/name").value as String
                user.email = snapshot.child("${userID}/email").value as String
                user.role=snapshot.child("${userID}/role").value as String

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                val bundle = Bundle()
                bundle.putSerializable("currentUserData", user)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                // ignore
            }
        })

        dbReference.child(userID)
    }

}