package com.lf.esharing

import android.app.AlertDialog
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
import androidx.lifecycle.ViewModel
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                            startSocketConnection()
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

    private fun startSocketConnection() {
        val socket = SocketIOClient.initInstance()
        SocketIOClient.connect(UserViewModel.username)
        socket?.on("JOIN_HOUSEHOLD_REQUEST", Emitter.Listener {
            val data = it[0] as String
            val jsonObject = JSONObject(data)
            val people = jsonObject.getString(SocketIOClient.FROM_USER)
            val owner = jsonObject.getString(SocketIOClient.TO_OWNER)
            alertDialog = AlertDialog.Builder(requireActivity().baseContext)
                .setTitle("Join Household Request")
                .setMessage(people + " want to join your household")
                .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Sorry, I dont want.")
                    dialogInterface.dismiss()
                })
                .setPositiveButton("Approve", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Okay let be together")
                    // TODO: add new member to household
                    dialogInterface.dismiss()
                })

            alertDialog.show()
        })
    }
}