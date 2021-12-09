package com.example.firebasetesting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbRealtime: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        //TODO: your DB URL here
        dbRealtime = FirebaseDatabase.getInstance()

        val editName: EditText = findViewById(R.id.editRegisterName)
        val editEmail: EditText = findViewById(R.id.editRegisterEmail)
        val editPassword: EditText = findViewById(R.id.editRegisterPassword)
        val btnSubmit: Button = findViewById(R.id.btnRegister)
        val btnGoToLogin: Button = findViewById(R.id.btnGoToLogin)

        btnSubmit.setOnClickListener {
            auth.createUserWithEmailAndPassword(
                editEmail.text.toString(),
                editPassword.text.toString()
            ).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "createUserWithEmail:success")

                    val newUser = User(
                        editName.text.toString(),
                        editEmail.text.toString(),
                        Roles.USER.toString()
                    )

                    Toast.makeText(this, "Updating database...", Toast.LENGTH_SHORT).show()

                    auth.currentUser?.uid?.let { it1 ->
                        dbRealtime.getReference("Users").child(it1).setValue(newUser)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Log.d("TAG", "addUserClassToDB:success")
                                } else {
                                    Log.d("TAG", "addUserClassToDB:failure")
                                    Toast.makeText(
                                        this,
                                        "We hit a snag... Try again",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }

                    val toast: Toast = Toast.makeText(
                        this,
                        "Whoop whoop! Account created!", Toast.LENGTH_SHORT
                    )
                    toast.show()

                    loadNewLoggedInUserUI(newUser)

                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    val toast: Toast = Toast.makeText(
                        this,
                        "Aw, authentication failed", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            }
        }

        btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            Toast.makeText(
                this,
                "Already logged in!", Toast.LENGTH_LONG
            ).show()
            loadExistingLoggedInUI()
        }
    }

    private fun loadNewLoggedInUserUI(userToPass: User) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("currentUserData", userToPass)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun loadExistingLoggedInUI() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}