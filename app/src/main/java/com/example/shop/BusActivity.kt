package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ActivityBusBinding
import com.example.shop.databinding.RowBusBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.row_bus.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import java.net.URL

class BusActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityBusBinding
    val bus_url = "https://data.tycg.gov.tw/opendata/datalist/datasetMeta/download?id=b3abedf0-aeae-4523-a804-6e807cbad589&rid=bf55b21a-2b7c-4ede-8048-f75420344aed"
    lateinit var buses: Buses

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBusBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        doAsync {
            val json = URL(bus_url).readText()
            buses = Gson().fromJson<Buses>(json, Buses::class.java)
            info(buses.datas.size)

            uiThread {
                val recycler = binding.recycler
                recycler.layoutManager = LinearLayoutManager(this@BusActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = BusAdapter()

            }
        }
    }

    inner class BusAdapter: RecyclerView.Adapter<BusHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus, parent, false)
            val holder = BusHolder(view)

            return holder
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            holder.busId.text = buses.datas.get(position).BusID
            holder.routeId.text = buses.datas.get(position).RouteID
            holder.speed.text = buses.datas.get(position).Speed
        }

        override fun getItemCount(): Int {
            return buses.datas.size
        }

    }
    class BusHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = RowBusBinding.bind(view)

        val busId = binding.busId
        val routeId = binding.routeId
        val speed = binding.speed
    }
}

data class Buses(
    val datas: List<Bus>
)

data class Bus(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val ledstate: String,
    val sections: String
)