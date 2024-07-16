package com.konka.uridemo.udpclient

import androidx.appcompat.app.AppCompatActivity
import com.konka.uridemo.databinding.ActivityUdpClientBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * @CreateTime : 2024/7/16 10:58
 * @Author : ljm
 * @Description :
 */
class UdpClientActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUdpClientBinding
    private lateinit var mainScope: CoroutineScope

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUdpClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainScope =  MainScope()
        binding.btnSend.setOnClickListener {
            val text = binding.etContent.text
            if (text.isNotEmpty()) {
                sendMsg(text.toString())
            }
        }
    }

    private fun sendMsg(msg: String) {
        mainScope.launch(Dispatchers.IO) {
            val socket = DatagramSocket()
            val buff = msg.toByteArray()
            val broadcastAddress = InetAddress.getByName("255.255.255.255")
            val packet = DatagramPacket(buff, buff.size, broadcastAddress, 8888)
            socket.send(packet)
        }
    }
}