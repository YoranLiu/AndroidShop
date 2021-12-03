package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signup.setOnClickListener { view ->
            val s_email = email.text.toString()
            val s_password = password.text.toString()
            if (s_email == "" || s_password == "") {
                AlertDialog.Builder(this)
                    .setTitle("Sing up message")
                    .setMessage("Email or password can not be empty")
                    .setPositiveButton("OK", null)
                    .show()
            }
            else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(s_email, s_password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            AlertDialog.Builder(this)
                                .setTitle("Sing up message")
                                .setMessage("Account has been successfully created")
                                .setPositiveButton("OK") { dialog, which ->
                                    val intent = Intent()
                                    intent.putExtra("EMAIL", s_email)
                                    intent.putExtra("PASSWORD", s_password)
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }
                                .show()
                        } else {
                            AlertDialog.Builder(this)
                                .setTitle("Sing up message")
                                .setMessage(it.exception?.message)
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    }
            }
        }
    }
}