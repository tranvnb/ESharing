package com.lf.esharing

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentLoginBinding
import com.lf.esharing.network.SocketIOClient
import com.lf.esharing.utils.NetworkHelper
import io.socket.emitter.Emitter
import org.json.JSONObject

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userViewModel: UserViewModel by viewModels()
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var btnGoReg = binding.root.findViewById<Button>(R.id.btnGoReg)
        btnGoReg.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_regFragment)
        }

        var btnLogin = binding.root.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            if (NetworkHelper.isNetworkConnected(requireContext())) {

                userViewModel.login(binding.edtxtUser.text.toString(), binding.edtxtPass.text.toString())
                    .observe(viewLifecycleOwner, Observer {
                        if (it == true) {
                            SocketIOClient.initInstance()
                            SocketIOClient.connect(UserViewModel.username)
                            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                        }else {
                            Toast.makeText(context, "Wrong username or password!", Toast.LENGTH_SHORT).show()
                        }
                    })

            }else{
                Toast.makeText(context, "No network connection.", Toast.LENGTH_LONG).show()
            }

        }
    }



}