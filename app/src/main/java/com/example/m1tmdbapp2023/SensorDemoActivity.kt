package com.example.m1tmdbapp2023

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m1tmdbapp2023.databinding.ActivitySensorDemoBinding

class SensorDemoActivity : AppCompatActivity() {
    lateinit var binding: ActivitySensorDemoBinding
    lateinit var sensorListAdapter: SensorListAdapter
    lateinit var sensorManager: SensorManager

    // data model for RV
    private val sensors: ArrayList<Sensor> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_demo)
        binding = ActivitySensorDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sensorListRv.setHasFixedSize(true)
        binding.sensorListRv.layoutManager = LinearLayoutManager(this)
        sensorListAdapter = SensorListAdapter(sensors)
        binding.sensorListRv.adapter = sensorListAdapter

        // get device's available sensors list
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensors.addAll(sensorManager.getSensorList(Sensor.TYPE_ALL))
        sensorListAdapter.notifyDataSetChanged()

    }
}