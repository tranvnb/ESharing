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

        binding.button.setOnClickListener {
            val socket = SocketIOClient.initInstance()
            SocketIOClient.connect("username1")
//            socket?.emit("JOIN_HOUSEHOLD_REQUEST", "basdfargasfasdfasdf")
//                val jObject: JSONObject = JSONObject("")
//                jObject.put(USERNAME, "username")
//                socket?.emit(ONLINE, jObject)
            socket?.emit("USER_ONLINE_CHANNEL", "username1")
            SocketIOClient.requestJoinHousehold(requireContext(), "username", "username1")
        }

        var btnLetGo = binding.root.findViewById<Button>(R.id.btnLetGo)
        btnLetGo.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(requireContext())) {
//                it.findNavController().navigate(R.id.loginFragment)
                // Testing socket functions
                val socket = SocketIOClient.initInstance()
                SocketIOClient.connect("username")
//                val jObject: JSONObject = JSONObject("")
//                jObject.put(USERNAME, "username")
//                socket?.emit(ONLINE, jObject)
                socket?.emit("USER_ONLINE_CHANNEL", "username")
                socket?.on("JOIN_HOUSEHOLD_REQUEST", Emitter.Listener {
                    val data = it[0] as String
                    val jsonObject = JSONObject(data)
                    val people = jsonObject.getString(SocketIOClient.FROM_USER)
                    val owner = jsonObject.getString(SocketIOClient.TO_OWNER)
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), "There is a request join your house from: " + people, Toast.LENGTH_SHORT).show()
                    }
                })

            }else{
                Toast.makeText(context, "No network connection.", Toast.LENGTH_LONG).show()
            }
        }
    }
}