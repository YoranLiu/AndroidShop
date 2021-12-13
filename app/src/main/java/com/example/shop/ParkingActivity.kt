package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.shop.databinding.ActivityParkingBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import java.net.URL

class ParkingActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityParkingBinding
    private val TAG = ParkingActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val parking_url = "https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=f4cc0b12-86ac-40f9-8745-885bddc18f79&rid=0daad6e6-0632-44f5-bd25-5e1de1e9146f"

        // Anko
        doAsync {
            val url = URL(parking_url)
            val json = url.readText()
            uiThread {
                binding.info.text = json
                info(json)
                toast("Json got")
                alert("Json got", "Json text") {
                    okButton {
                        parseGson(json)
                    }
                }.show()
            }

        }
//        lifecycleScope.launch {
//            val json = withContext(Dispatchers.IO) { readUrl(parking_url) }
//            Log.d(TAG, "onCreate: " + json)
//
//            binding.info.text = json
//        }
    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        info("Size: " + parking.parkingLots.size)

        parking.parkingLots.forEach {
            info("${it.areaId}  ${it.areaName}  ${it.parkId}  ${it.parkName}")
        }
    }

    private suspend fun readUrl(url: String): String {
        val json = URL(url).readText()

        return json
    }
}
data class Parking(
    val parkingLots: List<ParkingLot>
)

data class ParkingLot(
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)