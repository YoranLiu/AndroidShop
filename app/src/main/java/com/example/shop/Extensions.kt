package com.example.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

fun Activity.setNickname(nickname: String) {
    getSharedPreferences("Shop", AppCompatActivity.MODE_PRIVATE)
        .edit()
        .putString("NICKNAME", nickname)
        .apply()
}

fun Activity.getNickname(): String? {
    return  getSharedPreferences("Shop", AppCompatActivity.MODE_PRIVATE)
        .getString("NICKNAME", "")
}