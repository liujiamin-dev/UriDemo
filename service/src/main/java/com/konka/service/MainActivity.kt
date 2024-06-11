package com.konka.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.konka.service.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @CreateTime : 2024/5/31 17:55
 * @Author : Administrator
 * @Description :
 */
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSensorData()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(gravityListener)
        sensorManager.unregisterListener(gyroscopeListener)
    }

    private lateinit var sensorManager: SensorManager
    private fun getSensorData() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(gravityListener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(gyroscopeListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_UI)
    }

//    private val sensorEventListener = object: SensorEventListener{
//        override fun onSensorChanged(event: SensorEvent?) {
//            event?.apply {
//                if(sensor.type == Sensor.TYPE_GYROSCOPE) {
//                    Log.i(TAG, "onSensorChanged: ${event.values[2]}")
//                }
//            }
//        }
//
//        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//
//        }
//    }

    var mouseScrolling = false
    var sensitivity = 40  // 灵敏度

    var gravitySin = 0F
    var gravityCos = 0F

    private val gravityListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
                Log.i(TAG, "gravityListener onSensorChanged: ${event.values[0]}  ${event.values[1]}  ${event.values[2]}")
//            val gx = event.values[0]
//            val gz = event.values[2]
//            val gr = sqrt(gx.pow(2) + gz.pow(2))  // 重力方向
//            if (gr != 0f) {
//                gravitySin = gx / gr
//                gravityCos = gz / gr
//            } else {
//                gravitySin = 0f
//                gravityCos = 0f
//            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private val gyroscopeListener = object : SensorEventListener {
        val shake = 2
        override fun onSensorChanged(event: SensorEvent) {
//            Log.i(TAG, "gyroscopeListener onSensorChanged: ${event.values[0]} ${event.values[1]} ${event.values[2]}")
//            if (mouseScrolling) return
//            // 分别计算角速度在竖直方向和水平方向上的分量。
//            // 手机往右晃，鼠标往右移动，需要的x值为正，而对于陀螺仪在顺时针旋转的时候返回负值，故需要取反。
//            val x = -(event.values[2] * gravityCos + event.values[0] * gravitySin) * sensitivity
//            val y = -(event.values[0] * gravityCos - event.values[2] * gravitySin) * sensitivity
//            if (abs(x) <= shake && abs(y) <= shake) return
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

    }

    companion object {
        const val TAG = "MainActivity"
    }
}