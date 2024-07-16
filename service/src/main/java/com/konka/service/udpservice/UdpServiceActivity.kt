package com.konka.service.udpservice

import androidx.appcompat.app.AppCompatActivity
import com.konka.service.databinding.ActivityUdpServiceBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * @CreateTime : 2024/7/16 11:26
 * @Author : ljm
 * @Description :
 */
class UdpServiceActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUdpServiceBinding
    private lateinit var mainScope: CoroutineScope

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUdpServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainScope = MainScope()
        receiverMsg()
    }

    private fun receiverMsg() {
        mainScope.launch(Dispatchers.IO) {
            val socket = DatagramSocket(8888)
            val buff = ByteArray(1024)
            while (true) {
                val packet = DatagramPacket(buff, buff.size)
                socket.receive(packet)
                val msg = String(packet.data, 0, packet.length)
                launch(Dispatchers.Main) {
                    binding.tvContent.text = msg
                }
            }
        }
    }
}