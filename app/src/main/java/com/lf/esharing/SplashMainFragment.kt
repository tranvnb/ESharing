package com.lf.esharing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.lf.esharing.databinding.FragmentSplashMainBinding
import com.lf.esharing.network.SocketIOClient
import com.lf.esharing.network.SocketIOClient.ONLINE
import com.lf.esharing.network.SocketIOClient.USERNAME
import com.lf.esharing.utils.NetworkHelper
import io.socket.emitter.Emitter
import org.json.JSONObject

class SplashMainFragment : Fragment() {

    private lateinit var binding: FragmentSplashMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashMainBinding.inflate(inflater,container,false)
        //val v = inflater.inflate(R.layout.fragment_splash_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnLetGo = binding.root.findViewById<Button>(R.id.btnLetGo)
        btnLetGo.setOnClickListener {
                it.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}