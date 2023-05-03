package com.example.m1tmdbapp2023

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m1tmdbapp2023.databinding.ActivitySensorDemoBinding

class SensorDemoActivity : AppCompatActivity(), SensorEventListener, OnSensorItemClickListener {
    private val LOGTAG = SensorDemoActivity::class.simpleName
    lateinit var binding: ActivitySensorDemoBinding
    lateinit var sensorListAdapter: SensorListAdapter
    lateinit var sensorManager: SensorManager
    var currentSensor: Sensor? = null
    private val SAMPLING_INTERVAL_US = 1000000 // one second

    // data model for RV
    private val sensors: ArrayList<Sensor> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_demo)
        binding = ActivitySensorDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sensorListRv.setHasFixedSize(true)
        binding.sensorListRv.layoutManager = LinearLayoutManager(this)
        sensorListAdapter = SensorListAdapter(sensors,this)
        binding.sensorListRv.adapter = sensorListAdapter

        // get device's available sensors list
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensors.addAll(sensorManager.getSensorList(Sensor.TYPE_ALL))
        // sensorListAdapter.notifyDataSetChanged()
        sensorListAdapter.notifyItemRangeChanged(0, sensors.size)
        onSensorItemClicked(0)
    }

    override fun onSensorItemClicked(position: Int) {
        Log.d(LOGTAG,"onSensorItemClicked(${position})")
        // un register current sensor listener
        unRegisterSensorListener()
        // get selected sensor
        currentSensor = sensors[position]
        // register selected sensor listener
        Log.i(LOGTAG,"now listening to '${currentSensor?.name}'")
        registerSensorListener()
        binding.sensorDemoView.setCaption(currentSensor!!.name)
        binding.sensorDemoView.setSensorEvent(null)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.i(LOGTAG, "onSensorChanged(${event?.sensor?.id})")
        if (event != null) {
            val values = StringBuilder()
            val n = event.values.size
            for (i in 0 until n) {
                values.append("v[").append(i).append("]=").append(event.values[i])
                    .append(if (n - i == 1) '.' else ", ")
            }
            binding.sensorDemoView.setSensorEvent(event)
            binding.arrayValuesTv.text = values.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i(LOGTAG, "onAccuracyChanged($accuracy)")
    }

    private fun registerSensorListener() {
        currentSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SAMPLING_INTERVAL_US
            )
        }
    }

    private fun unRegisterSensorListener() {
        currentSensor?.let {
            sensorManager.unregisterListener(this)
        }
    }

    // activity life cycle management:
    // unregister sensors listener to save power
    override fun onResume() {
        super.onResume()
        registerSensorListener()
    }

    override fun onPause() {
        super.onPause()
        unRegisterSensorListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterSensorListener()
    }
}