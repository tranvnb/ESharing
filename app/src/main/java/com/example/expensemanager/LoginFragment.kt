package com.example.expensemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.expensemanager.databinding.FragmentLoginBinding
import com.example.expensemanager.databinding.FragmentRegistrationBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

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
            it.findNavController().navigate(R.id.registrationFragment)
        }

        var btnLogin = binding.root.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            it.findNavController().navigate(R.id.dashboardFragment)
        }
    }
}