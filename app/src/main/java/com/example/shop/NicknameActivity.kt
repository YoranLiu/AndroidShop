package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)


        finish.setOnClickListener { view ->
            val nick = nickname.text.toString()

            if (nick == "") {
                AlertDialog.Builder(this)
                    .setTitle("Nickname message")
                    .setMessage("Please type your nickname")
                    .setPositiveButton("OK", null)
                    .show()
            }
            else {
                AlertDialog.Builder(this)
                    .setTitle("Nickname message")
                    .setMessage("Set ${nick} as your nickname?")
                    .setPositiveButton("YES") { dialog, which ->
                        getSharedPreferences("Shop", MODE_PRIVATE)
                            .edit()
                            .putString("NICKNAME", nick)
                            .apply()
                        setResult(RESULT_OK)
                        Toast.makeText(this, "Nickname saved", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                    .setNeutralButton("NO", null)
                    .show()
            }
        }
    }
}