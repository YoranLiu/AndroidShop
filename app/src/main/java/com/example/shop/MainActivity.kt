package com.example.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ActivityMainBinding
import com.example.shop.databinding.RowFunctionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var signup = false
    val TAG = "MainActivity"
    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance()

    val functions = listOf<String>("Camera",
        "Invite friend",
        "Parking",
        "Download coupons",
        "News",
        "Movies",
        "Bus",
        "i",
        "j",
        "k",
        "l")

    var resultLauncherNickname =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val nick = getNickname()
                Log.d(TAG, "nickname: " + nick)
                //binding.contentMain.nickname.text = nick
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

    override fun onResume() {
        super.onResume()

        database.getReference("users")
            .child(auth.currentUser!!.uid)
            .child("nickname")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.contentMain.nickname.text = snapshot.value as String
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (!signup) {
//            val intent = Intent(this, SignUpActivity::class.java)
//            resultLauncherForSignUp.launch(intent)
//        }
        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // spinner
        val colors = arrayOf("R", "G", "B")
        val spinner = binding.contentMain.spinner

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, colors)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d(TAG, "onItemSelected: " + colors[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // recycler view
        val recycler = binding.contentMain.recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()
    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener {view ->
                functionClicked(holder, position)
            }
        }

        override fun getItemCount(): Int {
            return functions.size
        }

    }

    private fun functionClicked(holder: MainActivity.FunctionHolder, position: Int) {
        Log.d(TAG, "functionClicked: $position")

        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            5 -> startActivity(Intent(this, MovieActivity::class.java))
            6 -> startActivity(Intent(this, BusActivity::class.java))
        }
    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RowFunctionBinding.bind(view)
        val nameText: TextView = binding.name
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncherForSignUp.launch(intent)
        }
        else {
            Log.d(TAG, "authChanged: " + auth.currentUser?.uid)
        }

    }
}