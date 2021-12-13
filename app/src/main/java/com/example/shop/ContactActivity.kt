package com.example.shop

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.media.metrics.LogSessionId
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shop.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    private val RC_CONTACTS = 110

    private lateinit var binding: ActivityContactBinding
    private val TAG = ContactActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityContactBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // dangerous permission dealing
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            readContacts()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf( Manifest.permission.READ_CONTACTS), RC_CONTACTS)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts()
            }
        }
    }

    private fun readContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        while (cursor!!.moveToNext()) {
            val name =
                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            Log.d(TAG, "onCreate: " + name)
        }
    }
}