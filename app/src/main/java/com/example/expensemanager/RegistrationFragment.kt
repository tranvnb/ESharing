package com.example.expensemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.expensemanager.databinding.FragmentRegistrationBinding
import com.example.expensemanager.databinding.FragmentSplashMainBinding

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        //val v = inflater.inflate(R.layout.fragment_registration, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnRegSub = binding.root.findViewById<Button>(R.id.btnRegSub)
        btnRegSub.setOnClickListener {
            it.findNavController().navigate(R.id.loginFragment)
        }
    }
}