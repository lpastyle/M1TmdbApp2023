package com.example.m1tmdbapp2023

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m1tmdbapp2023.databinding.ActivitySensorDemoBinding

class SensorDemoActivity : AppCompatActivity() {
    lateinit var binding: ActivitySensorDemoBinding
    lateinit var sensorListAdapter: SensorListAdapter
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


    }
}