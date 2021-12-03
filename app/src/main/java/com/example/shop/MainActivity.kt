package com.example.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var signup = false
    val TAG = "MainActivity"

    var resultLauncherNickname =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val nick = getSharedPreferences("Shop", MODE_PRIVATE)
                    .getString("NICKNAME", "")
                Log.d(TAG, "nickname: " + nick)
            }
        }

    var resultLauncherForSignUp =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val s_email = result.data?.getStringExtra("EMAIL")
                val s_password = result.data?.getStringExtra("PASSWORD")
                Log.d(TAG, "E-Mail: $s_email\nPassword: $s_password")
                val intent = Intent(this, NicknameActivity::class.java)
                resultLauncherNickname.launch(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!signup) {
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncherForSignUp.launch(intent)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }



}